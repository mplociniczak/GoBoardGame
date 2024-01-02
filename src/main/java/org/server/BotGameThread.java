package org.server;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
            int X = 0;
            int Y = 0;

            while(true) {
                X = firstClientInput.readInt();
                Y = firstClientInput.readInt();

                if (board.buildBoard.isIntersectionEmpty(X, Y) && !board.buildBoard.isKoViolation()) {

                    board.placeBlackStone(X, Y);

                    sendMove(firstClientOutput, X, Y);

                } else {
                    sendMove(firstClientOutput, -1, -1);
                }

                Point p = bot.makeMove();
                sendMove(firstClientOutput, p.x , p.y);

            }
        } catch(IOException ex) {
            ex.getMessage();
        }
    }
}
