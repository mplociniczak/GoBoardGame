package org.client;

import org.server.StoneColor;
import org.server.StoneComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Actual version of a client - communicates with server
 */
public class ClientWithBoard extends JFrame implements Runnable {
    private JPanel gameBoardPanel;
    private JSplitPane splitPane;
    private JPanel scorePanel;
    private JLabel ter_B;  //Territory
    private JLabel ter_W;
    private JLabel pris_B;  //Prisoners
    private JLabel pris_W;
    private JLabel scr_B;  //Score
    private JLabel scr_W;
    private JButton pass;
    private final static int firstPlayer = 1;
    private final static int secondPlayer = 2;
    private final static int boardSize = 19;
    private ConnectionHandler connection;
    private JPanel[][]board = null;
    private boolean myTurn;

    public ClientWithBoard() {

        setTitle("Go Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);

        connection = new ConnectionHandler("localhost", 6680);

        createUI();
        setVisible(true);
    }

    private void createUI() {
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(0);

        gameBoardPanel = new JPanel();
        gameBoardPanel.setLayout(new GridLayout(boardSize, boardSize));
        gameBoardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        gameBoardPanel.addMouseListener(new ClickListener());

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                JPanel square = new JPanel() {

                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);

                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setColor(Color.BLACK);

                        // Grubsza linia pionowa na środku kwadratu
                        g2d.setStroke(new BasicStroke(2.0f));
                        g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

                        // Grubsza linia pozioma na środku kwadratu
                        g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
                    }
                };

                square.setBackground(Color.ORANGE);

                /* Zmiana koloru co drugiego pola
                if ((i + j) % 2 == 0) {
                    square.setBackground(Color.ORANGE);
                }

                 */
                square.setPreferredSize(new Dimension(25, 25));

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

        add(splitPane);
    }

    private Point convertCoordinatesToBoardIndex(int x, int y) {
        int tileSizeX = gameBoardPanel.getWidth() / boardSize;
        int tileSizeY = gameBoardPanel.getHeight() / boardSize;

        int X = x / tileSizeX;
        int Y = y / tileSizeY;

        // Ensure that the indices are within the valid range
        X = Math.max(0, Math.min(X, boardSize - 1));
        Y = Math.max(0, Math.min(Y, boardSize - 1));

        return new Point(X, Y);
    }

    private void updateStoneGraphics(int X, int Y, StoneColor color) {
        // Pobierz centralny kwadrat, który znajduje się na przecięciu czterech sąsiadujących kwadratów
        JPanel centralSquare = (JPanel) gameBoardPanel.getComponent(Y * boardSize + X);
        //JPanel squareToPaintRockOn = board[X][Y];
        // Usunięcie wcześniejszych komponentów z centralnego kwadratu
        centralSquare.removeAll();
        //squareToPaintRockOn.removeAll();

        // Oblicz położenie do narysowania koła na środku przecięcia
        int tileSize = gameBoardPanel.getWidth() / boardSize;
        int centerX = X * tileSize + tileSize / 2;
        int centerY = Y * tileSize + tileSize / 2;

        // Dodanie nowego komponentu reprezentującego kamień jako okrąg na środku przecięcia
        StoneComponent stoneComponent = new StoneComponent(color);
        int componentSize = stoneComponent.getPreferredSize().width;

        // Ustawienie rozmiaru kamienia
        stoneComponent.setSize(componentSize, componentSize);

        // Ustawienie pozycji kamienia na środku przecięcia
        int componentX = centerX - componentSize / 2;
        int componentY = centerY - componentSize / 2;

        stoneComponent.setBounds(componentX, componentY, componentSize, componentSize);
        centralSquare.add(stoneComponent);

        centralSquare.revalidate();
        centralSquare.repaint();
    }

    private void receiveCoordinatesAndPlaceStone(StoneColor color){
        Point receivedCoordinates = connection.receiveCoordinates();
        int X = (int) receivedCoordinates.getX();
        int Y = (int) receivedCoordinates.getY();
        System.out.println(X + " " + Y);

        //Incorrect move
        if(X == -1 && Y == -1) {
            System.out.println("Incorrect move!");
        } else {
            updateStoneGraphics(X, Y, color);
        }
    }
    @Override
    public void run() {
        int player = connection.receiveTurn();

        myTurn = (player == 1);

        System.out.println(player);

        int X = 0;
        int Y = 0;

        while (true) {
            if (player == firstPlayer) {
                //first player's move confirmation
                receiveCoordinatesAndPlaceStone(StoneColor.WHITE);

                //second player's move
                receiveCoordinatesAndPlaceStone(StoneColor.BLACK);

                myTurn = true;

            } else if (player == secondPlayer) {
                //first player's move confirmation
                receiveCoordinatesAndPlaceStone(StoneColor.WHITE);

                myTurn = true;

                //second player's move
                receiveCoordinatesAndPlaceStone(StoneColor.BLACK);

            }
        }
    }
    private class ClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(myTurn){
                Point boardIndices = convertCoordinatesToBoardIndex(e.getX(), e.getY());
                int X = (int) boardIndices.getX();
                int Y = (int) boardIndices.getY();
                connection.sendCoordinates(X, Y);
                myTurn = false;
            }
        }
    }
}
