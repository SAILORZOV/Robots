package gui;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class CoordinatesWindow extends MyWindow implements Observer {
    private final JLabel label;

    public CoordinatesWindow(RobotModel model) {
        super("Координаты робота", "CoordinatesWindow");
        label = new JLabel("X: 0, Y: 0");
        add(label);
        setSize(200, 100);
        model.addObserver(this);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof RobotModel model) {
            SwingUtilities.invokeLater(() -> {
                label.setText(String.format("X: %.2f, Y: %.2f", model.getX(), model.getY()));
            });
        }
    }
}
