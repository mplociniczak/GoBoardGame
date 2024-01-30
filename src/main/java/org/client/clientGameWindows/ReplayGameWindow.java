package org.client.clientGameWindows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.constants.ConstantVariables.endgameCode;
import static org.server.gameLogic.Board.size;

public class ReplayGameWindow extends JFrame implements Runnable {

    private Utils draw;
    private JPanel gameBoardPanel;
    public ReplayGameWindow(){

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
                //replayGameForwards();
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
        // TODO: we need coordinates and color from database to call a function that updates graphics drawing one stone at every button click
        //draw.updateStoneGraphics(X, Y, color, gameBoardPanel);
    }
    private void replayGameBackwards(){
        //TODO: idk how to do it
    }
    @Override
    public void run() {

    }
}
