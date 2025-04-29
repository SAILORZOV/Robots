package programLogic;

public class RobotDataStructure {
    public RobotDataStructure(double positionX, double positionY, double direction, int targetX, int targetY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.direction = direction;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public double positionX = 0;
    public double positionY = 0;
    public double direction = 0;
    public int targetX = 0;
    public int targetY = 0;
}
