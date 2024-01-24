package org.server.bot;

import org.server.Server;
import org.server.gameLogic.Board;
import org.server.gameLogic.StoneColor;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.server.Server.*;

public class BotGameThread implements Runnable {
    ObjectInputStream firstClientInput;
    ObjectOutputStream firstClientOutput;
    private Board board;
    private SmartBot bot;
    public BotGameThread(ObjectInputStream firstClientInput, ObjectOutputStream firstClientOutput) {
        this.firstClientInput = firstClientInput;
        this.firstClientOutput = firstClientOutput;

        board = new Board();
        bot = new SmartBot();
    }

    private void sendMove(ObjectOutputStream out, int X, int Y) throws IOException {
        System.out.println(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.writeObject(board.BoardToStringBuilderWithStoneColors(X, Y));
        out.flush();
    }
    @Override
    public void run() {
        try {
            int X;
            int Y;

            while(true) {
                X = firstClientInput.readInt();
                Y = firstClientInput.readInt();

                if (board.buildBoard.isIntersectionEmpty(X, Y)) {

                    board.placeStone(X, Y, StoneColor.BLACK, StoneColor.WHITE);

                    sendMove(firstClientOutput, X, Y);

                } else {
                    sendMove(firstClientOutput, errorCode, errorCode);
                }

                Point p = bot.makeMove();
                sendMove(firstClientOutput, p.x , p.y);

            }
        } catch(IOException ex) {
            ex.getMessage();
        }
    }
}
