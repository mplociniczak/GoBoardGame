package org.server.bot;

import org.server.gameLogic.Board;
import org.server.gameLogic.StoneColor;
import org.server.iGameThread;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.constants.ConstantVariables.*;

/**
 * The BotGameThread class represents a thread for a game against a bot.
 * It handles communication with the first client and orchestrates the game moves against the SmartBot.
 */
public class BotGameThread implements Runnable, iGameThread {
    ObjectInputStream firstClientInput;
    ObjectOutputStream firstClientOutput;
    private Board board;
    private SmartBot bot;

    /**
     * Constructs a new BotGameThread with the specified input and output streams for the first client.
     *
     * @param firstClientInput  The input stream for the first client.
     * @param firstClientOutput The output stream for the first client.
     */
    public BotGameThread(ObjectInputStream firstClientInput, ObjectOutputStream firstClientOutput) {
        this.firstClientInput = firstClientInput;
        this.firstClientOutput = firstClientOutput;

        board = new Board();
        bot = new SmartBot();
    }

    /**
     * Sends the current board state to the specified output stream.
     *
     * @param out The output stream to send the board state to.
     * @param X   The X-coordinate of the last move.
     * @param Y   The Y-coordinate of the last move.
     * @throws IOException If an I/O error occurs during the output operation.
     */
    public void sendMove(ObjectOutputStream out, int X, int Y) throws IOException {
        out.writeObject(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.flush();
    }

    /**
     * Runs the BotGameThread, handling the game logic and communication with the first client.
     */
    @Override
    public void run() {
        try {
            Point coordinates;

            int[] playersMove = new int[2];

            while(true) {

                coordinates = (Point) firstClientInput.readObject();

                if (board.buildBoard.isValidCoordinate(coordinates.x, coordinates.y) && board.buildBoard.isIntersectionEmpty(coordinates.x, coordinates.y)) {

                    playersMove[0] = coordinates.x;
                    playersMove[1] = coordinates.y;

                    board.placeStone(playersMove, StoneColor.BLACK, StoneColor.WHITE);

                    sendMove(firstClientOutput, playersMove[0], playersMove[1]);

                } else if (coordinates.x == passCode) {

                    sendMove(firstClientOutput, endgameCode, endgameCode);

                    break;

                } else {

                    sendMove(firstClientOutput, errorCode, errorCode);

                }

                coordinates = bot.makeMove();
                board.placeStone(coordinates.x, coordinates.y, StoneColor.WHITE, StoneColor.BLACK);
                sendMove(firstClientOutput, coordinates.x , coordinates.y);

            }
        } catch(IOException | ClassNotFoundException ex) {
            ex.getMessage();
        }
    }
}
