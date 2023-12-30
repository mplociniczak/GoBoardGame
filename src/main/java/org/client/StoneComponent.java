package org.client;

import org.server.StoneColor;

import javax.swing.*;
import java.awt.*;

// Klasa reprezentująca komponent kamienia (okrąg)
public class StoneComponent extends JComponent {
    private StoneColor color;

    public StoneComponent(StoneColor color) {
        this.color = color;

        setPreferredSize(new Dimension(15, 15));
    }

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