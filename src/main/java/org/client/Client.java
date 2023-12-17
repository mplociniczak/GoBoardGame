package org.client;
import org.server.StoneColor;
import org.server.State;
import org.server.Stone;
import org.server.StoneComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Client extends JFrame implements Runnable {
    private ConnectionHandler connection;
    private JButton connectButton;
    private JButton joinGameButton;
    private JButton startGameButton;
    private JFrame gameBoardFrame;
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
    private int X;
    private int Y;
    private boolean myTurn = true;
    private boolean waiting = true;
    private final static int firstPlayer = 1;
    private final static int secondPlayer = 2;

    private Stone[][] board; // Dwuwymiarowa tablica reprezentująca planszę

    public Client() {
        setTitle("Go Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);

        createUI();
        initializeBoard();
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

    private void initializeBoard() {
        int size = 19; // Rozmiar planszy

        board = new Stone[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Stone();
            }
        }
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
        while (true) {
            if (player == firstPlayer) {
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
            Point boardIndices = convertCoordinatesToBoardIndex(e.getX(), e.getY(), 19);
            int row = (int) boardIndices.getX();
            int col = (int) boardIndices.getY();
            System.out.println(row + " " + col);

            if (board[row][col].getState() == State.EMPTY) {
                StoneColor currentColor = myTurn ? StoneColor.BLACK : StoneColor.WHITE;
                board[row][col].placeStone(currentColor);

                // Aktualizacja wyglądu graficznego
                updateStoneGraphics(row, col, currentColor);

                // Dodaj kod do przesłania informacji o ruchu do serwera
                connection.sendCoordinates(row, col);

                // Oznacz, że teraz jest ruch przeciwnika
                myTurn = false;
            } else {
                JOptionPane.showMessageDialog(null, "This intersection is already occupied.");
            }
        }
    }

    private void updateStoneGraphics(int row, int col, StoneColor color) {
        // Pobierz centralny kwadrat, który znajduje się na przecięciu czterech sąsiadujących kwadratów
        JPanel centralSquare = (JPanel) gameBoardPanel.getComponent(row * 19 + col);

        // Usunięcie wcześniejszych komponentów z centralnego kwadratu
        centralSquare.removeAll();

        // Oblicz położenie do narysowania koła na środku przecięcia
        int tileSize = gameBoardPanel.getWidth() / 19; // Zakładając, że 19 to rozmiar planszy
        int centerX = col * tileSize + tileSize / 2;
        int centerY = row * tileSize + tileSize / 2;

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
