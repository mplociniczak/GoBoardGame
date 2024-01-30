package org.client;

import org.client.clientGameWindows.GameWindow;
import org.client.clientGameWindows.ReplayGameWindow;
import org.server.database.Game;
import org.server.database.GameDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Starting window with 3 buttons to choose what you want to do
 */
public class Client extends JFrame {

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

        JButton playWithBot = new JButton("Play with bot");
        JButton replayGame = new JButton("Replay Game");
        JButton startGameButton = new JButton("Start Game");

        playWithBot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for playing with bot
                Thread t = new Thread(new GameWindow(1));
                t.start();
            }
        });

        replayGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for replaying a game
                chooseAndReplayGame();
            }
        });

        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for starting a game
                Thread t = new Thread(new GameWindow(0));
                t.start();
            }
        });

        mainPanel.add(playWithBot);
        mainPanel.add(replayGame);
        mainPanel.add(startGameButton);

        add(mainPanel);
    }

    private void chooseAndReplayGame() {
        GameDAO gameDAO = new GameDAO();
        List<Game> games = gameDAO.getAllGames();
        String[] gameOptions = games.stream().map(game -> "Game ID: " + game.getId()).toArray(String[]::new);

        String selectedGame = (String) JOptionPane.showInputDialog(
                this,
                "Choose a game to replay:",
                "Select Game",
                JOptionPane.QUESTION_MESSAGE,
                null,
                gameOptions,
                gameOptions[0]
        );

        if (selectedGame != null) {
            Long gameId = Long.parseLong(selectedGame.replace("Game ID: ", ""));
            ReplayGameWindow replayWindow = new ReplayGameWindow(gameId);
            replayWindow.setVisible(true);
        }
    }
}
