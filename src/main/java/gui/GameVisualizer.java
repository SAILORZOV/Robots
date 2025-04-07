package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel {
    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    private RobotModel model;

    public GameVisualizer(RobotModel model) {
        this.model = model;

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.updateModel();
            }
        }, 0, 10);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                EventQueue.invokeLater(GameVisualizer.this::repaint);
            }
        }, 0, 50);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setTargetPosition(e.getX(), e.getY());
                setTargetPosition(e.getPoint());
                repaint();
            }
        });

        setDoubleBuffered(true);
    }

    protected void setTargetPosition(Point p) {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, model.getX(), model.getY(), model.getDirection());
        drawTarget(g2d, m_targetPositionX, m_targetPositionY);
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
}
