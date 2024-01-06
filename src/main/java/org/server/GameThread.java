package org.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.net.Socket;


/**
 * The GameThread class represents a thread that manages a Go game between two clients.
 * It handles the communication between clients, updates the game board, and saves the moves to a database.
 */
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

    /**
     * Constructs a GameThread object with the specified input and output streams for both clients.
     *
     * @param firstClientInput  The input stream for the first client.
     * @param firstClientOutput The output stream for the first client.
     * @param secondClientInput The input stream for the second client.
     * @param secondClientOutput The output stream for the second client.
     */
    public GameThread(ObjectInputStream firstClientInput, ObjectOutputStream firstClientOutput, ObjectInputStream secondClientInput, ObjectOutputStream secondClientOutput) {
        this.firstClientOutput = firstClientOutput;
        this.secondClientOutput = secondClientOutput;
        this.firstClientInput = firstClientInput;
        this.secondClientInput = secondClientInput;
        board = new Board();
        entityManagerFactory = Persistence.createEntityManagerFactory("GoPersistenceUnit");
    }

    /**
     * Sends a move to the specified output stream with the given coordinates.
     *
     * @param out The output stream to send the move to.
     * @param X   The X-coordinate of the move.
     * @param Y   The Y-coordinate of the move.
     * @throws IOException If an I/O error occurs while sending the move.
     */
    private void sendMove(ObjectOutputStream out, int X, int Y) throws IOException {
        System.out.println(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.writeObject(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.flush();
    }

    /**
     * Saves the move information to the database.
     *
     * @param turnNumber The turn number of the move.
     * @param x          The X-coordinate of the move.
     * @param y          The Y-coordinate of the move.
     * @param color      The color of the stone placed in the move.
     */
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

    /**
     * Replays the game from the database, updating the board with the recorded moves.
     */
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

    /**
     * Gets the opposite color of the specified stone color.
     *
     * @param color The stone color.
     * @return The opposite stone color.
     */
    private StoneColor getOppositeColor(StoneColor color) {
        return (color == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;
    }
}
