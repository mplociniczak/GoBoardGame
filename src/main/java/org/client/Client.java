//package org.client;
//import org.server.StoneColor;
//import org.server.Stone;
//import org.client.StoneComponent;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//
///**
// * Doesn't communicate with server - check what can we improve
// */
//public class Client extends JFrame implements Runnable {
//    private ConnectionHandler connection;
//    private JButton connectButton;
//    private JButton joinGameButton;
//    private JButton startGameButton;
//    private JFrame gameBoardFrame;
//    private JPanel gameBoardPanel;
//    private JSplitPane splitPane;
//    private JPanel scorePanel;
//    private JLabel ter_B;  //Territory
//    private JLabel ter_W;
//    private JLabel pris_B;  //Prisoners
//    private JLabel pris_W;
//    private JLabel scr_B;  //Score
//    private JLabel scr_W;
//    private JButton pass;
//    private boolean myTurn = true;
//    private boolean waiting = true;
//    private final static int firstPlayer = 1;
//    private final static int secondPlayer = 2;
//    private final static int size = 19;
//
//    private Stone[][] board; // Dwuwymiarowa tablica reprezentująca planszę
//
//    public Client() {
//        setTitle("Go Game");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(400, 200);
//
//        createUI();
//        initializeBoard();
//    }
//
//    private void createUI() {
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new GridLayout(3, 1));
//
//        connectButton = new JButton("Connect to Server");
//        joinGameButton = new JButton("Join Game");
//        startGameButton = new JButton("Start Game");
//
//        connectButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Implement the logic for connecting to the server
//                JOptionPane.showMessageDialog(null, "Connecting to server...");
//            }
//        });
//
//        joinGameButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Implement the logic for joining a game
//                JOptionPane.showMessageDialog(null, "Joining the game...");
//            }
//        });
//
//        startGameButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Implement the logic for starting a game
//                connection = new ConnectionHandler("localhost", 6670);
//                showGameBoard(size);  // Change the size as needed
//            }
//        });
//
//        mainPanel.add(connectButton);
//        mainPanel.add(joinGameButton);
//        mainPanel.add(startGameButton);
//
//        add(mainPanel);
//    }
//
//    private void initializeBoard() {
//
//        board = new Stone[size][size];
//
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                board[i][j] = new Stone();
//            }
//        }
//    }
//
//    private void showGameBoard(int size) {
//        gameBoardFrame = new JFrame("Go Game Board");
//        gameBoardFrame.setSize(900, 550);
//        gameBoardFrame.setBackground(Color.darkGray);
//
//        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        splitPane.setDividerSize(0);
//
//        gameBoardPanel = new JPanel();
//        gameBoardPanel.setLayout(new GridLayout(size, size));
//        gameBoardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
//
//        gameBoardPanel.addMouseListener(new ClickListener());
//
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                JPanel square = new JPanel();
//                square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//                square.setPreferredSize(new Dimension(25, 25));
//
//                // zmiana koloru co drugiego pola
//                if ((i + j) % 2 == 0) {
//                    square.setBackground(Color.ORANGE);
//                }
//
//                gameBoardPanel.add(square);
//            }
//        }
//
//        splitPane.setLeftComponent(gameBoardPanel);
//
//        ter_B = new JLabel();
//        ter_W = new JLabel();
//        pris_B = new JLabel();
//        pris_W = new JLabel();
//        scr_B = new JLabel();
//        scr_W = new JLabel();
//        pass = new JButton("Pass");
//
//        scorePanel = new JPanel();
//        scorePanel.setLayout(new GridLayout(5, 3)); // Two columns
//        scorePanel.setBackground(Color.LIGHT_GRAY);
//
//        scorePanel.add(new JLabel());           //Upper left corner of the scorePanel is empty
//        JLabel black = new JLabel("Black");
//        Font font = new Font(black.getFont().getFontName(), Font.BOLD, 16);
//        black.setFont(font);
//        scorePanel.add(black);
//
//        JLabel white = new JLabel("White");
//        white.setForeground(Color.WHITE);
//        white.setFont(font);
//        scorePanel.add(white);
//
//        scorePanel.add(new JLabel("Territory"));
//        scorePanel.add(ter_B);
//        scorePanel.add(ter_W);
//
//        scorePanel.add(new JLabel("Prisoners"));
//        scorePanel.add(pris_B);
//        scorePanel.add(pris_W);
//
//        scorePanel.add(new JLabel("Score"));
//        scorePanel.add(scr_B);
//        scorePanel.add(scr_W);
//
//        scorePanel.add(pass);
//
//        splitPane.add(scorePanel);
//
//        gameBoardFrame.add(splitPane);
//        gameBoardFrame.setResizable(false);
//        gameBoardFrame.setVisible(true);
//    }
//    private void receiveCoordinatesAndPlaceStone(StoneColor color){
//        Point receivedCoordinates = connection.receiveCoordinates();
//        int X = (int) receivedCoordinates.getX();
//        int Y = (int) receivedCoordinates.getY();
//        System.out.println(X + " " + Y);
//
//        //Incorrect move
//        if(X == -1 && Y == -1) {
//            System.out.println("Incorrect move!");
//        } else {
//            board[X][Y].placeStone(color);
//            updateStoneGraphics(X, Y, color);
//        }
//    }
//    @Override
//    public void run() {
//        int player = connection.receiveTurn();
//        System.out.println(player);
//
//        Point receivedCoordinates;
//        int X = 0;
//        int Y = 0;
//
//        while (true) {
//            if (player == firstPlayer) {
//                //first player's move confirmation
//                //receiveCoordinatesAndPlaceStone(StoneColor.WHITE);
//                System.out.println("First player waiting...");
//                try {
//                    connection.waitForPlayerAction(waiting);
//                } catch (InterruptedException ex) {
//                    //TODO: handle
//                }
//                receivedCoordinates = connection.receiveCoordinates();
//                X = (int) receivedCoordinates.getX();
//                Y = (int) receivedCoordinates.getY();
//                updateStoneGraphics(X, Y, StoneColor.WHITE);
//                System.out.println("Board updated!");
//
//                //second player's move
//                //receiveCoordinatesAndPlaceStone(StoneColor.BLACK);
//                receivedCoordinates = connection.receiveCoordinates();
//                X = (int) receivedCoordinates.getX();
//                Y = (int) receivedCoordinates.getY();
//                updateStoneGraphics(X, Y, StoneColor.BLACK);
//
//
//            } else if (player == secondPlayer) {
//                //first player's move confirmation
//                //receiveCoordinatesAndPlaceStone(StoneColor.BLACK);
//                receivedCoordinates = connection.receiveCoordinates();
//                X = (int) receivedCoordinates.getX();
//                Y = (int) receivedCoordinates.getY();
//                updateStoneGraphics(X, Y, StoneColor.WHITE);
//
//                try {
//                    connection.waitForPlayerAction(waiting);
//                } catch (InterruptedException ex) {
//                    //TODO: handle
//                }
//                //second player's move
//                //receiveCoordinatesAndPlaceStone(StoneColor.WHITE);
//                receivedCoordinates = connection.receiveCoordinates();
//                X = (int) receivedCoordinates.getX();
//                Y = (int) receivedCoordinates.getY();
//                updateStoneGraphics(X, Y, StoneColor.BLACK);
//
//            }
//        }
//    }
//    private Point convertCoordinatesToBoardIndex(int x, int y) {
//        int tileSize = gameBoardPanel.getWidth() / size;
//
//        int row = y / tileSize;
//        int col = x / tileSize;
//
//        // Ensure that the indices are within the valid range
//        row = Math.max(0, Math.min(row, size - 1));
//        col = Math.max(0, Math.min(col, size - 1));
//
//        return new Point(row, col);
//    }
//
//    private class ClickListener extends MouseAdapter {
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            //if(myTurn){
//                Point boardIndices = convertCoordinatesToBoardIndex(e.getX(), e.getY());
//                int X = (int) boardIndices.getX();
//                int Y = (int) boardIndices.getY();
//                connection.sendCoordinates(X, Y);
//            //}
//        }
//    }
//
//    private void updateStoneGraphics(int row, int col, StoneColor color) {
//        // Pobierz centralny kwadrat, który znajduje się na przecięciu czterech sąsiadujących kwadratów
//        JPanel centralSquare = (JPanel) gameBoardPanel.getComponent(row * 19 + col);
//
//        // Usunięcie wcześniejszych komponentów z centralnego kwadratu
//        centralSquare.removeAll();
//
//        // Oblicz położenie do narysowania koła na środku przecięcia
//        int tileSize = gameBoardPanel.getWidth() / size;
//        int centerX = col * tileSize + tileSize / 2;
//        int centerY = row * tileSize + tileSize / 2;
//
//        // Dodanie nowego komponentu reprezentującego kamień jako okrąg na środku przecięcia
//        StoneComponent stoneComponent = new StoneComponent(color);
//        int componentSize = stoneComponent.getPreferredSize().width;
//
//        // Ustawienie rozmiaru kamienia
//        stoneComponent.setSize(componentSize, componentSize);
//
//        // Ustawienie pozycji kamienia na środku przecięcia
//        int componentX = centerX - componentSize / 2;
//        int componentY = centerY - componentSize / 2;
//
//        stoneComponent.setBounds(componentX, componentY, componentSize, componentSize);
//        centralSquare.add(stoneComponent);
//
//        centralSquare.revalidate();
//        centralSquare.repaint();
//    }
//}
