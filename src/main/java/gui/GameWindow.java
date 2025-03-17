package gui;

import java.awt.*;
import javax.swing.JPanel;

public class GameWindow extends MyWindow {
    private final GameVisualizer m_visualizer;

    public GameWindow() {
        super("Игровое поле", "gameWindow");
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        setLocation(10, 10);
        setSize(400, 400);
        setMinimumSize(getSize());
        pack();
    }
}
