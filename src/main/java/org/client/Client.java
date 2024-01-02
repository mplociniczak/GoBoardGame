package org.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Doesn't communicate with server - check what can we improve
 */
public class Client extends JFrame {
    private JButton playWithBot;
    private JButton joinGameButton;
    private JButton startGameButton;

    public Client() {
        setTitle("Go Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);

        createUI();
        setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        playWithBot = new JButton("Play with bot");
        joinGameButton = new JButton("Join Game");
        startGameButton = new JButton("Start Game");

        playWithBot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for playing with bot
                Thread t = new Thread(new ClientWithBoard(1));
                t.start();
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
                Thread t = new Thread(new ClientWithBoard(0));
                t.start();
            }
        });

        mainPanel.add(playWithBot);
        mainPanel.add(joinGameButton);
        mainPanel.add(startGameButton);

        add(mainPanel);
    }
}
