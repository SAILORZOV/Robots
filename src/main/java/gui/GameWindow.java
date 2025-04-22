package gui;

import programLogic.GameVisualizer;
import programLogic.RobotModel;

import java.awt.*;
import javax.swing.JPanel;

public class GameWindow extends MyWindow {
    private final GameVisualizer m_visualizer;

    public GameWindow(RobotModel model) {
        super("Игровое поле", "gameWindow");
        minHeight = 400;
        minWidth = 400;
        m_visualizer = new GameVisualizer(model);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        setLocation(10, 10);
        setSize(400, 400);
        setMinimumSize(getSize());
        pack();
    }
}
