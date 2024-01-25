package org.client;

import org.server.gameLogic.StoneColor;

import javax.swing.*;
import java.awt.*;

/**
 * The StoneComponent class represents a graphical component for displaying a Go game stone on the game board.
 * It extends JComponent and provides a visual representation of a stone with a specified color.
 */
public class StoneComponent extends JComponent {
    private StoneColor color;

    /**
     * Constructs a StoneComponent with the specified color.
     *
     * @param color The color of the stone (StoneColor.BLACK or StoneColor.WHITE).
     */
    public StoneComponent(StoneColor color) {
        this.color = color;

        setPreferredSize(new Dimension(15, 15));
    }

    /**
     * Overrides the paintComponent method to customize the rendering of the stone on the component.
     *
     * @param g The Graphics object used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Włączenie Antialiasing
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Ustaw kolor rysowania
        if (color == StoneColor.BLACK) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.WHITE);
        }

        // Narysuj wypełniony okrąg
        g.fillOval(0, 0, getWidth(), getHeight());
    }
}