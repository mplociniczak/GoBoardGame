package org.client;

import org.server.*;
import org.server.gameLogic.Board;
import org.server.gameLogic.Stone;
import org.server.gameLogic.StoneColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static org.server.Server.*;
import static org.server.gameLogic.Board.*;

/**
 * The {@code ClientWithBoard} class represents the client-side of a Go game with a graphical user interface.
 * It communicates with the server using the {@link ConnectionHandler} class.
 *
 * <p>
 * The client includes a game board, score panel, and buttons for passing and replaying the game.
 * It supports both two-player games and games against a bot.
 * </p>
 *
 * <p>
 * The game board is displayed as a grid of squares, and players can make moves by clicking on the squares.
 * The game state is updated in real-time based on the server's responses.
 * </p>
 *
 * <p>
 * The client also utilizes the {@link ScoreCalculator} to calculate and display the scores based on the game state.
 * </p>
 *
 * @author MP, RR
 * @version 1.0
 */
public class ClientWithBoard extends JFrame implements Runnable {
    private static JPanel gameBoardPanel;
    private JLabel ter_B;  //Territory
    private JLabel ter_W;
    private JLabel pris_B;  //Prisoners
    private JLabel pris_W;
    private JLabel scr_B;  //Score
    private JLabel scr_W;
    private boolean passClicked = false;
    private final int gameOption;
    private JButton pass;
    private JButton replayButton;
    private int gameOption;
    private final static int firstPlayer = 1;
    private final static int secondPlayer = 2;
    private final ConnectionHandler connection;
    private boolean myTurn;
    private static Stone[][] fields;
    private ScoreCalculator scoreCalculator; // Add ScoreCalculator instance


    /**
     * Constructs a new {@code ClientWithBoard} with the specified game option.
     *
     * @param gameOption The game option: 0 for a two-player game, 1 for a game against a bot.
     */
    public ClientWithBoard(int gameOption) {

        setTitle("Go Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);

        this.gameOption = gameOption;

        connection = new ConnectionHandler("localhost", port, gameOption);

        fields = new Stone[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[i][j] = new Stone();
            }
        }

        createUI();
        setVisible(true);
        scoreCalculator = new ScoreCalculator(fields, ter_B, ter_W, pris_B, pris_W, scr_B, scr_W);

    }


    /**
     * Creates the graphical user interface components, including the game board, score panel, and buttons.
     */
    private void createUI() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(0);

        gameBoardPanel = new JPanel();
        gameBoardPanel.setLayout(new GridLayout(size, size));
        gameBoardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        gameBoardPanel.addMouseListener(new ClickListener());

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
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
        JButton pass = new JButton("Pass");

        JPanel scorePanel = new JPanel();
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

        pass.addActionListener(e -> {
            passClicked = true;
            connection.sendCoordinates(passCode, passCode);
        });

        replayButton = new JButton("Replay Game");
        replayButton.addActionListener(e -> replayGame());
        scorePanel.add(replayButton);

        splitPane.add(scorePanel);

        add(splitPane);
    }


    /**
     * Converts pixel coordinates to board indices.
     *
     * @param x The x-coordinate in pixels.
     * @param y The y-coordinate in pixels.
     * @return A {@link Point} representing the board indices.
     */
    private Point convertCoordinatesToBoardIndex(int x, int y) {
        int tileSizeX = gameBoardPanel.getWidth() / size;
        int tileSizeY = gameBoardPanel.getHeight() / size;

        // Ensure that the indices are within the valid range
        int X = Math.max(0, Math.min(x / tileSizeX, size - 1));
        int Y = Math.max(0, Math.min(y / tileSizeY, size - 1));

        return new Point(X, Y);
    }


    /**
     * Updates the graphical representation of a stone on the game board.
     *
     * @param X     The x-coordinate of the stone on the board.
     * @param Y     The y-coordinate of the stone on the board.
     * @param color The color of the stone.
     */
    private void updateStoneGraphics(int X, int Y, StoneColor color) {
        // Pobierz centralny kwadrat, który znajduje się na przecięciu czterech sąsiadujących kwadratów
        JPanel centralSquare = (JPanel) gameBoardPanel.getComponent(Y * size + X);

        // Usunięcie wcześniejszych komponentów z centralnego kwadratu
        centralSquare.removeAll();
        //squareToPaintRockOn.removeAll();

        // Oblicz położenie do narysowania koła na środku przecięcia
        int tileSize = gameBoardPanel.getWidth() / size;
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

        removeStoneFromBoard();
    }


    /**
     * Removes stones marked as "REMOVED" from the game board.
     */
    private void removeStoneFromBoard() {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(fields[i][j].getColor().equals(StoneColor.REMOVED)) {
                    JPanel centralSquare = (JPanel) gameBoardPanel.getComponent(j * size + i);

                    centralSquare.removeAll();

                    centralSquare.revalidate();

                    centralSquare.repaint();
                }
            }
        }
    }


    /**
     * Receives coordinates from the server, updates the game state, and places a stone on the board.
     *
     * @param color The color of the stone to be placed.
     */
    private void receiveCoordinatesAndPlaceStone(StoneColor color){
        StringBuilder receivedCoordinates = connection.receiveCoordinates();

        System.out.println(receivedCoordinates);

        String[] receivedString = receivedCoordinates.toString().split(" ");

        int X = Integer.parseInt(receivedString[0]);
        int Y = Integer.parseInt(receivedString[1]);

        receivedString[1] = null;
        receivedString[0] = null;

        int ctr = 2;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[i][j].setColor(StoneColor.valueOf(receivedString[ctr++]));
                System.out.print(fields[i][j].getColor() + " ");
            }
        }
        System.out.print("\n");

        //Incorrect move
        if(X == errorCode) {
            System.out.println("Incorrect move!");
            JOptionPane.showMessageDialog(null, "Miejsce zajęte. Wybierz inne.", "Błąd ruchu", JOptionPane.ERROR_MESSAGE);
        } else if(X == passCode) {
            myTurn = false;
            passClicked = false;
        } else {
            updateStoneGraphics(X, Y, color);
            scoreCalculator.updateScoreLabels();
        }
    }

    /**
     * Runs the client thread, managing the game loop and handling user input.
     */
    @Override
    public void run() {

        //2 players game
        if(gameOption == 0){
            int player = connection.receiveTurn();

            myTurn = (player == firstPlayer);

            System.out.println(player);

            while (true) {
                if (player == firstPlayer) {
                    //first player's move confirmation
                    receiveCoordinatesAndPlaceStone(StoneColor.BLACK);

                    //second player's move
                    receiveCoordinatesAndPlaceStone(StoneColor.WHITE);

                    myTurn = true;

                } else if (player == secondPlayer) {
                    //first player's move confirmation
                    receiveCoordinatesAndPlaceStone(StoneColor.BLACK);

                    myTurn = true;

                    //second player's move
                    receiveCoordinatesAndPlaceStone(StoneColor.WHITE);

                }
            }
        }
        //Game with bot
        else if(gameOption == 1) {
            myTurn = true;

            while(true) {
                receiveCoordinatesAndPlaceStone(StoneColor.BLACK);
                receiveCoordinatesAndPlaceStone(StoneColor.WHITE);
                myTurn = true;
            }
        }
    }

    /**
     * The {@code ClickListener} class represents a mouse click listener for the game board.
     */
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

    /**
     * Initiates the replay of the game.
     */
    private void replayGame() {
        // TODO
    }
}
