package org.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Client extends JFrame implements Runnable {
    ConnectionHandler connection;
    private JButton connectButton;
    private JButton joinGameButton;
    private JButton startGameButton;
    private JFrame gameBoardFrame;
    private JPanel gameBoardPanel;
    private JSplitPane splitPane;
    private JPanel scorePanel;
    JLabel ter_B;  //Territory
    JLabel ter_W;
    JLabel pris_B;  //Prisoners
    JLabel pris_W;
    JLabel scr_B;  //Score
    JLabel scr_W;
    JButton pass;
    private int X;
    private int Y;
    private boolean myTurn = true;
    private boolean waiting = true;
    private final static int firstPlayer = 1;
    private final static int secondPlayer = 2;
    public Client() {
        setTitle("Go Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);

        createUI();
    }

    private void createUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        connectButton = new JButton("Connect to Server");
        joinGameButton = new JButton("Join Game");
        startGameButton = new JButton("Start Game");

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for connecting to the server
                JOptionPane.showMessageDialog(null, "Connecting to server...");
            }
        });

        joinGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for joining a game
                JOptionPane.showMessageDialog(null, "Joining the game...");
            }
        });

        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for starting a game
                connection = new ConnectionHandler("192.168.56.1", 6670);
                showGameBoard(19);  // Change the size as needed
            }
        });

        mainPanel.add(connectButton);
        mainPanel.add(joinGameButton);
        mainPanel.add(startGameButton);

        add(mainPanel);
    }

    private void showGameBoard(int size) {
        gameBoardFrame = new JFrame("Go Game Board");
        gameBoardFrame.setSize(900, 550);
        gameBoardFrame.setBackground(Color.darkGray);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(0);

        gameBoardPanel = new JPanel();
        gameBoardPanel.setLayout(new GridLayout(size, size));
        gameBoardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        gameBoardPanel.addMouseListener(new ClickListener());

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JPanel square = new JPanel();
                square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                square.setPreferredSize(new Dimension(25, 25));

                // zmiana koloru co drugiego pola
                if ((i + j) % 2 == 0) {
                    square.setBackground(Color.ORANGE);
                }

                gameBoardPanel.add(square);
            }
        }

        splitPane.setLeftComponent(gameBoardPanel);

        ter_B = new JLabel();
        ter_W = new JLabel();
        pris_B = new JLabel();
        pris_W = new JLabel();
        scr_B = new JLabel();
        scr_W = new JLabel();
        pass = new JButton("Pass");

        scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(5, 3)); // Two columns
        scorePanel.setBackground(Color.LIGHT_GRAY);

        scorePanel.add(new JLabel());           //Upper left corner of the scorePanel is empty
        JLabel black = new JLabel("Black");
        Font font = new Font(black.getFont().getFontName(), Font.BOLD, 16);
        black.setFont(font);
        scorePanel.add(black);

        JLabel white = new JLabel("White");
        white.setForeground(Color.WHITE);
        white.setFont(font);
        scorePanel.add(white);

        scorePanel.add(new JLabel("Territory"));
        scorePanel.add(ter_B);
        scorePanel.add(ter_W);

        scorePanel.add(new JLabel("Prisoners"));
        scorePanel.add(pris_B);
        scorePanel.add(pris_W);

        scorePanel.add(new JLabel("Score"));
        scorePanel.add(scr_B);
        scorePanel.add(scr_W);

        scorePanel.add(pass);

        splitPane.add(scorePanel);

        gameBoardFrame.add(splitPane);
        gameBoardFrame.setResizable(false);
        gameBoardFrame.setVisible(true);
    }

    @Override
    public void run() {
        int player = connection.receiveTurn();
        while(true) {
            if(player == firstPlayer) {
                Point receivedCoordinates = connection.receiveCoordinates();
                X = (int) receivedCoordinates.getX();
                Y = (int) receivedCoordinates.getY();
                //metoda setStone
//                try {
//                    connection.waitForPlayerAction(waiting);
//                } catch (InterruptedException ex) {
//                    //TODO: handle
//                }

            } else if (player == secondPlayer) {
//                try {
//                    connection.waitForPlayerAction(waiting);
//                } catch (InterruptedException ex) {
//                    //TODO: handle
//                }
                Point receivedCoordinates = connection.receiveCoordinates();
                X = (int) receivedCoordinates.getX();
                Y = (int) receivedCoordinates.getY();
            }

        }
    }

    private Point convertCoordinatesToBoardIndex(int x, int y, int boardSize) {
        int tileSize = gameBoardPanel.getWidth() / boardSize;

        int row = y / tileSize;
        int col = x / tileSize;

        // Ensure that the indices are within the valid range
        row = Math.max(0, Math.min(row, boardSize - 1));
        col = Math.max(0, Math.min(col, boardSize - 1));

        return new Point(row, col);
    }


    private class ClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Point boardIndices = convertCoordinatesToBoardIndex(e.getX(), e.getY(), 19); // Change the size as needed
            int row = (int) boardIndices.getX();
            int col = (int) boardIndices.getY();
            System.out.println(row + "  " + col);
            connection.sendCoordinates(row, col);
                //myTurn = false;
            //}
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Client gui = new Client();
                gui.setVisible(true);
            }
        });
    }
}
