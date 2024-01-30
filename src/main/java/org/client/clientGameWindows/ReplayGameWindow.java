package org.client.clientGameWindows;

import org.server.database.Move;
import org.server.database.MoveDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static org.constants.ConstantVariables.endgameCode;
import static org.server.gameLogic.Board.size;

public class ReplayGameWindow extends JFrame implements Runnable {

    private Utils draw;
    private JPanel gameBoardPanel;

    private Long gameId; // ID gry do odtworzenia
    private List<Move> moves; // Lista ruchów w grze
    private int currentMoveIndex = 0; // Indeks aktualnego ruchu

    public ReplayGameWindow(Long gameId){
        this.gameId = gameId;

        // Pobierz ruchy dla gry
        MoveDAO moveDAO = new MoveDAO();
        this.moves = moveDAO.getMovesByGameId(gameId);

        setTitle("Go Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);

        draw = new DrawingUtils();

        createUI();

        setResizable(false);

        setVisible(true);
    }

    private void createUI() {

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(0);

        gameBoardPanel = new JPanel();
        gameBoardPanel.setLayout(new GridLayout(size, size));
        gameBoardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

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

                square.setPreferredSize(new Dimension(25, 25));

                gameBoardPanel.add(square);
            }
        }

        splitPane.setLeftComponent(gameBoardPanel);

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(5, 3)); // Two columns
        scorePanel.setBackground(Color.LIGHT_GRAY);

        JButton previous = new JButton("Prevoius move");
        JButton next = new JButton("Next move");

        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Previous");
                //replayGameBackwards();
            }
        });

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Next");
                replayGameForwards();
            }
        });

        scorePanel.add(previous);
        scorePanel.add(next);

        splitPane.setRightComponent(scorePanel);

        add(splitPane);
    }

    /**
     * Initiates the replay of the game.
     */
    private void replayGameForwards() {
        if (currentMoveIndex < moves.size()) {
            Move currentMove = moves.get(currentMoveIndex);
            // Aktualizuj planszę na podstawie ruchu
            draw.updateStoneGraphics(currentMove.getPositionX(), currentMove.getPositionY(), currentMove.getStoneColor(), gameBoardPanel);
            currentMoveIndex++;
        } else {
            // Wyświetl komunikat, gdy nie ma więcej ruchów do odtworzenia
            JOptionPane.showMessageDialog(this, "Wszystkie ruchy zostały odtworzone. Nie ma więcej ruchów do wyświetlenia.", "Koniec ruchów", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void replayGameBackwards(){
        //TODO: idk how to do it
        //TODO: me neither XD
    }

    @Override
    public void run() {

    }
}
