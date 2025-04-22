package programLogic;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;


public class GameVisualizer extends JPanel implements Observer {
    private volatile double
            positionX = 100,
            positionY = 100,
            direction = 0;

    private volatile int
            targetX = 100,
            targetY = 100;

    private final java.util.Timer m_timer = initTimer();

    private static java.util.Timer initTimer() {
        return new Timer("redraw generator", true);
    }

    public GameVisualizer(RobotModel model) {

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                EventQueue.invokeLater(GameVisualizer.this::repaint);
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setTargetPosition(e.getX(), e.getY());
            }
        });

        model.addObserver(this);
        setDoubleBuffered(true);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, positionX, positionY, direction);
        drawTarget(g2d, targetX, targetY);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, double x, double y, double direction) {
        int cx = (int) Math.round(x);
        int cy = (int) Math.round(y);
        AffineTransform t = AffineTransform.getRotateInstance(direction, cx, cy);
        g.setTransform(t);

        g.setColor(Color.MAGENTA);
        g.fillOval(cx - 15, cy - 5, 30, 10);
        g.setColor(Color.BLACK);
        g.drawOval(cx - 15, cy - 5, 30, 10);
        g.setColor(Color.WHITE);
        g.fillOval(cx + 10, cy - 2, 5, 5);
        g.setColor(Color.BLACK);
        g.drawOval(cx + 10, cy - 2, 5, 5);
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof RobotModel && arg instanceof RobotDataStructure structure) {
            SwingUtilities.invokeLater(() -> {
                positionX = structure.positionX;
                positionY = structure.positionY;
                direction = structure.direction;
                targetX = structure.targetX;
                targetY = structure.targetY;
            });
        }
    }
}
