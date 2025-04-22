package programLogic;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;


public class RobotModel extends Observable {
    private volatile double positionX = 100;
    private volatile double positionY = 100;
    private volatile double direction = 0;

    private volatile int targetX = 150;
    private volatile int targetY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;


    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        return new Timer("robot update generator", true);
    }

    public RobotModel() {
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateModel();
            }
        }, 0, 10);
    }

    public void setTargetPosition(int x, int y) {
        targetX = x;
        targetY = y;
    }

    public void updateModel() {
        double distance = distance(targetX, targetY, positionX, positionY);
        if (distance < 0.5) return;

        double velocity = maxVelocity;
        double angleToTarget = angleTo(positionX, positionY, targetX, targetY);
        double angularDiff = normalizeAngle(angleToTarget - direction);
        double angularVelocity = applyLimits(angularDiff * 0.5, -maxAngularVelocity, maxAngularVelocity);

        moveRobot(velocity, angularVelocity, 10);
        setChanged();
        notifyObservers(new RobotDataStructure(positionX, positionY, direction, targetX, targetY));
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);

        double newX = positionX + velocity / angularVelocity *
                (Math.sin(direction + angularVelocity * duration) - Math.sin(direction));
        if (!Double.isFinite(newX)) {
            newX = positionX + velocity * duration * Math.cos(direction);
        }

        double newY = positionY - velocity / angularVelocity *
                (Math.cos(direction + angularVelocity * duration) - Math.cos(direction));
        if (!Double.isFinite(newY)) {
            newY = positionY + velocity * duration * Math.sin(direction);
        }

        positionX = newX;
        positionY = newY;
        direction = normalizeAngle(direction + angularVelocity * duration);
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        return Math.atan2(toY - fromY, toX - fromX);
    }

    private static double normalizeAngle(double angle) {
        while (angle < -Math.PI) angle += 2 * Math.PI;
        while (angle > Math.PI) angle -= 2 * Math.PI;
        return angle;
    }

    private static double applyLimits(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

//    public double getX() {
//        return positionX;
//    }
//
//    public double getY() {
//        return positionY;
//    }
//
//    public double getDirection() {
//        return direction;
//    }
}
