package org.client;

import org.server.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Actual version of a client - communicates with server
 */
public class ClientWithBoard extends JFrame implements Runnable {
    private static JPanel gameBoardPanel;
    private JSplitPane splitPane;
    private JPanel scorePanel;
    private JLabel ter_B;  //Territory
    private JLabel ter_W;
    private JLabel pris_B;  //Prisoners
    private JLabel pris_W;
    private JLabel scr_B;  //Score
    private JLabel scr_W;
    private JButton pass;
    private boolean passClicked = false;

    private int gameOption;
    private final static int firstPlayer = 1;
    private final static int secondPlayer = 2;
    private final static int boardSize = 19;
    private final ConnectionHandler connection;
    private boolean myTurn;
    private static Stone[][] fields;
    private ScoreCalculator scoreCalculator; // Add ScoreCalculator instance

    public ClientWithBoard(int gameOption) {

        setTitle("Go Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);

        this.gameOption = gameOption;

        connection = new ConnectionHandler("localhost", Server.port, gameOption);

        fields = new Stone[19][19];
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                fields[i][j] = new Stone();
            }
        }

        createUI();
        setVisible(true);
        scoreCalculator = new ScoreCalculator(fields, ter_B, ter_W, pris_B, pris_W, scr_B, scr_W);

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

        pass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePassButton();
            }
        });

        splitPane.add(scorePanel);

        add(splitPane);
    }

    private Point convertCoordinatesToBoardIndex(int x, int y) {
        int tileSizeX = gameBoardPanel.getWidth() / boardSize;
        int tileSizeY = gameBoardPanel.getHeight() / boardSize;

        // Ensure that the indices are within the valid range
        int X = Math.max(0, Math.min(x / tileSizeX, boardSize - 1));
        int Y = Math.max(0, Math.min(y / tileSizeY, boardSize - 1));

        return new Point(X, Y);
    }

    private void updateStoneGraphics(int X, int Y, StoneColor color) {
        // Pobierz centralny kwadrat, który znajduje się na przecięciu czterech sąsiadujących kwadratów
        JPanel centralSquare = (JPanel) gameBoardPanel.getComponent(Y * boardSize + X);

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

        removeStoneFromBoard();
    }

    //metoda do usuwania kamienia na GUI
    private void removeStoneFromBoard() {
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                if(fields[i][j].getColor().equals(StoneColor.REMOVED)) {
                    JPanel centralSquare = (JPanel) gameBoardPanel.getComponent(j * boardSize + i);

                    centralSquare.removeAll();

                    centralSquare.revalidate();

                    centralSquare.repaint();
                }
            }
        }
    }

    private void receiveCoordinatesAndPlaceStone(StoneColor color){
        StringBuilder receivedCoordinates = connection.receiveCoordinates();

        System.out.println(receivedCoordinates);

        String receivedString[] = receivedCoordinates.toString().split(" ");

        int X = Integer.parseInt(receivedString[0]);
        int Y = Integer.parseInt(receivedString[1]);

        receivedString[1] = null;
        receivedString[0] = null;

        int ctr = 2;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                fields[i][j].setColor(StoneColor.valueOf(receivedString[ctr++]));
                System.out.print(fields[i][j].getColor() + " ");
            }
        }
        System.out.print("\n");

        //Incorrect move
        if(X == -1 && Y == -1) {
            System.out.println("Incorrect move!");
            JOptionPane.showMessageDialog(null, "Miejsce zajęte. Wybierz inne.", "Błąd ruchu", JOptionPane.ERROR_MESSAGE);
        } else {
            updateStoneGraphics(X, Y, color);
            scoreCalculator.updateScoreLabels();
        }
    }
    @Override
    public void run() {

        //2 players game
        if(gameOption == 0){
            int player = connection.receiveTurn();

            myTurn = (player == 1);

            System.out.println(player);

            while (true) {
                if (player == firstPlayer) {
                    receiveCoordinatesAndPlaceStone(StoneColor.BLACK);
                    myTurn = true;

                    // Dodaj warunek sprawdzający naciśnięcie przycisku "Pass"
                    if (passClicked) {
                        passClicked = false; // Zresetuj flagę naciśnięcia przycisku
                        // Zmiana tury na przeciwnika
                        myTurn = false;
                        continue; // Pomiń resztę pętli, aby uniknąć dodatkowego ruchu
                    }

                    // Przesyłanie informacji o pasie
                    receiveCoordinatesAndPlaceStone(StoneColor.WHITE);

                } else if (player == secondPlayer) {
                    // Przesyłanie informacji o pasie
                    receiveCoordinatesAndPlaceStone(StoneColor.BLACK);

                    myTurn = true;

                    // Dodaj warunek sprawdzający naciśnięcie przycisku "Pass"
                    if (passClicked) {
                        passClicked = false; // Zresetuj flagę naciśnięcia przycisku
                        // Zmiana tury na przeciwnika
                        myTurn = false;
                        continue; // Pomiń resztę pętli, aby uniknąć dodatkowego ruchu
                    }

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

    private void handlePassButton() {
        if (myTurn) {
            // Wysyłanie informacji o pasie do serwera
            connection.sendCoordinates(-1, -1);

            // Zmiana tury na przeciwnika
            myTurn = false;

            // Zresetuj flagę naciśnięcia przycisku "Pass"
            passClicked = false;
        } else {
            // Komunikat, że nie jest teraz twoja tura
            JOptionPane.showMessageDialog(null, "Nie jest teraz twoja tura.", "Błąd tury", JOptionPane.ERROR_MESSAGE);
        }
    }



    private class ClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (myTurn) {
                Point boardIndices = convertCoordinatesToBoardIndex(e.getX(), e.getY());
                int X = (int) boardIndices.getX();
                int Y = (int) boardIndices.getY();

                if (!passClicked) {
                    connection.sendCoordinates(X, Y);
                } else {
                    // Jeśli przycisk "Pass" został wcześniej naciśnięty, wysyłamy specjalne współrzędne -1, -1
                    connection.sendCoordinates(-1, -1);
                }

                myTurn = false;
            }
        }
    }

}
