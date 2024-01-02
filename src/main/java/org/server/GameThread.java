package org.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.net.Socket;

public class GameThread implements Runnable {
    private EntityManagerFactory entityManagerFactory;
    private final static int first = 1;
    private final static int second = 2;
    private final static int errorCode = -1;
    private static int turn = first;
    ObjectOutputStream firstClientOutput;
    ObjectOutputStream secondClientOutput;
    ObjectInputStream firstClientInput;
    ObjectInputStream secondClientInput;
    private Board board;
    public GameThread(ObjectInputStream firstClientInput, ObjectOutputStream firstClientOutput, ObjectInputStream secondClientInput, ObjectOutputStream secondClientOutput) {
        this.firstClientOutput = firstClientOutput;
        this.secondClientOutput = secondClientOutput;
        this.firstClientInput = firstClientInput;
        this.secondClientInput = secondClientInput;
        board = new Board();
        entityManagerFactory = Persistence.createEntityManagerFactory("GoPersistenceUnit");
    }

    private void sendMove(ObjectOutputStream out, int X, int Y) throws IOException {
        System.out.println(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.writeObject(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.flush();
    }

    private void saveMoveToDatabase(int turnNumber, int x, int y, StoneColor color) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Move move = new Move();
        move.setGame(this); // Ustaw aktualną grę
        move.setTurnNumber(turnNumber);
        move.setXCoordinate(x);
        move.setYCoordinate(y);
        move.setPlayerColor(color);

        entityManager.persist(move);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void run() {

        int X;
        int Y;

        Server.addGame(this);
        System.out.println("Running...");

        try{
            firstClientOutput.writeInt(first);
            firstClientOutput.flush();

            secondClientOutput.writeInt(second);
            secondClientOutput.flush();

            while (true) {
                if (turn == first) {

                    X = firstClientInput.readInt();
                    Y = firstClientInput.readInt();

                    if (board.buildBoard.isIntersectionEmpty(X, Y)) {

                        board.placeStone(X, Y, StoneColor.BLACK, StoneColor.WHITE);

                        sendMove(secondClientOutput, X, Y);
                        sendMove(firstClientOutput, X, Y);

                        // Zapisz ruch do bazy danych
                        saveMoveToDatabase(turn, X, Y, StoneColor.BLACK);
                    } else {
                        sendMove(firstClientOutput, errorCode, errorCode);
                        sendMove(secondClientOutput, errorCode, errorCode);
                    }

                    turn = second;

                }

                if (turn == second) {
                    X = secondClientInput.readInt();
                    Y = secondClientInput.readInt();

                    if (board.buildBoard.isIntersectionEmpty(X, Y)) {

                        board.placeStone(X, Y, StoneColor.WHITE, StoneColor.BLACK);

                        sendMove(firstClientOutput, X, Y);
                        sendMove(secondClientOutput, X, Y);

                        // Zapisz ruch do bazy danych
                        saveMoveToDatabase(turn, X, Y, StoneColor.WHITE);
                    } else {
                        sendMove(secondClientOutput, errorCode, errorCode);
                        sendMove(firstClientOutput, errorCode, errorCode);
                    }

                    turn = first;

                }
            }

        } catch (IOException ex) {
            //TODO: handle
        }
    }

    private void replayGameFromDatabase() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Move> moves = entityManager.createQuery("SELECT m FROM Move m WHERE m.game = :game ORDER BY m.turnNumber", Move.class)
                .setParameter("game", this)
                .getResultList();

        for (Move move : moves) {
            // Odtwórz ruch na planszy
            board.placeStone(move.getXCoordinate(), move.getYCoordinate(), move.getPlayerColor(), getOppositeColor(move.getPlayerColor()));
        }

        entityManager.close();
    }

    private StoneColor getOppositeColor(StoneColor color) {
        return (color == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;
    }
}
