import javafx.scene.shape.Circle;
import model.Brain;

import java.util.ArrayList;

class Player{
    final static String[] sensorArray = new String[]{"sensor_0", "sensor_1", "sensor_2", "sensor_3", "sensor_4", "sensor_5", "sensor_6", "sensor_7"};
    static Double[] sensorValuesArray =  new Double[]{10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0};
    final ArrayList<Obstacle> obstacles;

    public double position_x = 0.0;
    public double position_y = 0.0;
    final double speed;
    public double lastAngle = 0.0;

    public double old_position_x = position_x;
    public double old_position_y = position_y;

    private final double[] mapCords;
    private final Brain brain;
    private final static double sensorAngle = 45;

    public Circle avatar;

    public Player(double[] mapCords, String pathToDriver, ArrayList<Obstacle> obstacles, double speed){
        this.mapCords = mapCords;
        this.brain = new Brain(pathToDriver, sensorArray.length);
        this.obstacles = obstacles;
        this.speed = speed;
        this.avatar = new Circle(position_x, position_y, (mapCords[2] - mapCords[0])/20);
    }

    public void checkSensors(){
        for(int i=0; i<8; i++){
            double distance = 10.0;
            for(Obstacle obstacle : obstacles){
                double x1 = position_x;
                double y1 = position_y;
                double x2 = obstacle.x;
                double y2 = obstacle.y;
                double x3 = position_x + 20 * Math.cos(Math.toRadians(i * 45));
                double y3 = position_y + 20 * Math.sin(Math.toRadians(i * 45));
                if( MyMath.distance(x1, y1, x2, y2) + MyMath.distance(x2, y2, x3, y3) - (20.0 / Math.cos(Math.toRadians(22.5)))  /*MyMath.distance(x1, y1, x3, y3)*/ < (double) 0.00001  ){
                    distance = Math.min( MyMath.distance(x1, y1, x2, y2), distance );
                }
            }
            sensorValuesArray[i] = distance;
        }
    }

    public void move(){
        checkSensors();
        try {
            double angle = brain.calculateResultDouble(sensorArray, sensorValuesArray, "movement");
            lastAngle = angle;

            old_position_x = position_x;
            old_position_y = position_y;

            double x_movement = Math.cos(Math.toRadians(angle)) * speed;
            double y_movement = Math.sin(Math.toRadians(angle)) * speed;
            this.position_x += x_movement;
            this.position_y += y_movement;

            mapCords[0] += x_movement;      // Player will be centered on the map
            mapCords[1] += y_movement;
            mapCords[2] += x_movement;
            mapCords[3] += y_movement;

            this.avatar.setCenterX(this.position_x);
            this.avatar.setCenterY(this.position_y);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}