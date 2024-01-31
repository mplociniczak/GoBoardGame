package org.client.clientGameWindows;

import org.server.database.Move;
import org.server.database.MoveDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static org.constants.ConstantVariables.*;

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

        draw.drawEmptyGameBoard(gameBoardPanel);

        splitPane.setLeftComponent(gameBoardPanel);

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(5, 3)); // Two columns
        scorePanel.setBackground(Color.LIGHT_GRAY);

        JButton previous = new JButton("Prevoius move");
        JButton next = new JButton("Next move");

        previous.addActionListener(e -> {
            System.out.println("Previous");
            replayGameBackwards();
        });

        next.addActionListener(e -> {
            System.out.println("Next");
            replayGameForwards();
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
            int currentXCoordinate = currentMove.getPositionX(); // Pobierz współrzędną X dla bieżącego ruchu

            if (currentXCoordinate >= 0) { // Sprawdź, czy współrzędna X nie jest ujemna
                // Aktualizuj planszę na podstawie ruchu
                draw.updateStoneGraphics(currentXCoordinate, currentMove.getPositionY(), currentMove.getStoneColor(), gameBoardPanel);
                currentMoveIndex++; // Inkrementacja indeksu po użyciu, aby uniknąć błędu przy ostatnim ruchu
            } else {
                // Współrzędna X jest ujemna, obsłuż tę sytuację
                JOptionPane.showMessageDialog(this, "Wszystkie ruchy zostały odtworzone. Nie ma więcej ruchów do wyświetlenia.", "Koniec ruchów", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (currentMoveIndex >= moves.size()) {
            // Wyświetl komunikat, gdy nie ma więcej ruchów do odtworzenia
            JOptionPane.showMessageDialog(this, "Wszystkie ruchy zostały odtworzone. Nie ma więcej ruchów do wyświetlenia.", "Koniec ruchów", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void replayGameBackwards() {
        if (currentMoveIndex > 0) {
            // Cofnij indeks ruchu
            currentMoveIndex--;

            gameBoardPanel.removeAll();
            // Wyczyść planszę
            draw.drawEmptyGameBoard(gameBoardPanel);

            gameBoardPanel.revalidate();
            gameBoardPanel.repaint();

            // Rysuj wszystkie ruchy do currentMoveIndex - 1
            for (int i = 0; i < currentMoveIndex; i++) {
                Move move = moves.get(i);
                draw.updateStoneGraphics(move.getPositionX(), move.getPositionY(), move.getStoneColor(), gameBoardPanel);
            }
        } else {
            // Wyświetl komunikat, gdy nie ma więcej ruchów do odtworzenia
            JOptionPane.showMessageDialog(this, "Wszystkie ruchy zostały cofnięte. Nie ma więcej ruchów do wyświetlenia.", "Koniec ruchów", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    @Override
    public void run() {}
}
