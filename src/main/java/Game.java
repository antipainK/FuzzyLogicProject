import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Brain;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game extends Application{
    private static final int screenWidth = 1000;
    private static final int screenHeight = 1000;
    private static final double fps = 30;

    private static final int obstaclesAmount = 5;
    private static final int playerDiameter = (screenWidth + screenHeight)/2 /20;
    private static final int obstacleDiameter = (screenWidth + screenHeight)/2 /40;
    private static final double playerSpeed = 0.15;
    private static final double obstacleSpeed = 0.25;

    static String pathToDriver;

    public static void main(String[] args){
        pathToDriver = args[0];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FuzzyGame - Wojciech Kosztyla");
        Group root = new Group();
        Scene scene = new Scene( root );
        primaryStage.setScene( scene );

        Canvas canvas = new Canvas( screenWidth, screenHeight );
        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();



        ArrayList<Obstacle> obstacles = new ArrayList<>();

        double mapStart_x = -10.0;
        double mapStart_y = -10.0;
        double mapEnd_x = 10.0;
        double mapEnd_y = 10.0;
        double outOfBoundsRange = 2.0;
        double[] mapCords = new double[]{mapStart_x, mapStart_y, mapEnd_x, mapEnd_y, outOfBoundsRange};

        Player player = new Player(mapCords, pathToDriver, obstacles, playerSpeed);

        new AnimationTimer()
        {
            long startFrameTime = System.nanoTime();

            public void handle(long currentNanoTime)
            {
                int frameJump = (int) Math.floor((System.nanoTime() - startFrameTime) / (1000000000 / fps));
                if(frameJump >= 1){
                    startFrameTime = System.nanoTime();

                    if(obstacles.size() < obstaclesAmount){      // Create new obstacles if there isn't enough
                        double randomPoint_x, randomPoint_y;
                        double temp_angle = Math.floor(Math.random() * 8) * Math.PI / 4;
                        temp_angle = Math.random() * Math.PI * 2;
                        randomPoint_x = mapCords[0] + 10 + Math.cos(temp_angle) * Math.sqrt( Math.pow(mapCords[2] - mapCords[0], 2) + Math.pow(mapCords[3] - mapCords[1], 2) ) / 2;
                        randomPoint_y = mapCords[1] + 10 +  Math.sin(temp_angle) * Math.sqrt( Math.pow(mapCords[2] - mapCords[0], 2) + Math.pow(mapCords[3] - mapCords[1], 2) ) / 2;

                        obstacles.add(new Obstacle(randomPoint_x, randomPoint_y, MyMath.angle(randomPoint_x, randomPoint_y, player.position_x, player.position_y), obstacleSpeed));

                        //System.out.println("New obstacle: Position: " + randomPoint_x + "," + randomPoint_y + " Rotation in angles: " + MyMath.angle(randomPoint_x, randomPoint_y, player.position_x, player.position_y) + " MAPCORDS: " + mapCords[0] + " " + mapCords[1]);
                    }

                    int i = 0;
                    while(i < obstacles.size()){     // Move all obstacles
                        Obstacle obstacle = obstacles.get(i);
                        obstacle.move();
                        if(MyMath.distance(obstacle.x, obstacle.y, player.position_x, player.position_y) > 1.2 * Math.sqrt( Math.pow(mapCords[2] - mapCords[0], 2) + Math.pow(mapCords[3] - mapCords[1], 2) ) / 2 ){
                            obstacles.remove(obstacle);     // Remove obstacle, if out of the map
                        }
                        else{
                            i++;
                        }
                    }

                    player.move();  // Move player

                    //// DISPLAYING PART
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());  // Clear screen

                    int[] tempOld = MyMath.translate(player.old_position_x, player.old_position_y, mapCords, canvas.getWidth(), canvas.getHeight());
                    gc.setFill(Color.ALICEBLUE);
                    gc.fillOval(tempOld[0] - (int)(playerDiameter/2), tempOld[1] - (int)(playerDiameter/2), playerDiameter, playerDiameter);

                    int[] temp = MyMath.translate(player.position_x, player.position_y, mapCords, canvas.getWidth(), canvas.getHeight());
                    if(player.lastAngle <= 360)
                    {
                        gc.beginPath();
                        gc.moveTo(temp[0], temp[1]);
                        double angle = Math.toRadians(player.lastAngle);
                        gc.lineTo(temp[0] - (int)(Math.cos(angle) * playerDiameter * 2), temp[1] - (int)(Math.sin(angle) * playerDiameter * 2));
                        gc.lineTo(temp[0] - (int)(Math.cos(angle) * playerDiameter * 2) + (int)(Math.cos(angle - 45) * playerDiameter / 2), temp[1] - (int)(Math.sin(angle) * playerDiameter * 2) + (int)(Math.sin(angle - 45) * playerDiameter / 2) );
                        gc.lineTo(temp[0] - (int)(Math.cos(angle) * playerDiameter * 2) + (int)(Math.cos(angle + 45) * playerDiameter / 2), temp[1] - (int)(Math.sin(angle) * playerDiameter * 2) + (int)(Math.sin(angle + 45) * playerDiameter / 2) );
                        gc.lineTo(temp[0] - (int)(Math.cos(angle) * playerDiameter * 2), temp[1] - (int)(Math.sin(angle) * playerDiameter * 2));
                        gc.stroke();
                    }
                    gc.setFill(Color.BLACK);
                    gc.fillOval(temp[0] - (int)(playerDiameter/2), temp[1] - (int)(playerDiameter/2), playerDiameter, playerDiameter);

                    for(Obstacle obstacle : obstacles){
                        temp = MyMath.translate(obstacle.x, obstacle.y, mapCords, canvas.getWidth(), canvas.getHeight());
                        gc.beginPath();
                        gc.moveTo(temp[0], temp[1]);
                        double angle = Math.toRadians(obstacle.angle);
                        gc.lineTo(temp[0] - (int)(Math.cos(angle) * obstacleDiameter * 2), temp[1] - (int)(Math.sin(angle) * obstacleDiameter * 2));
                        gc.lineTo(temp[0] - (int)(Math.cos(angle) * obstacleDiameter * 2) + (int)(Math.cos(angle - 45) * obstacleDiameter / 2), temp[1] - (int)(Math.sin(angle) * obstacleDiameter * 2) + (int)(Math.sin(angle - 45) * obstacleDiameter / 2) );
                        gc.lineTo(temp[0] - (int)(Math.cos(angle) * obstacleDiameter * 2) + (int)(Math.cos(angle + 45) * obstacleDiameter / 2), temp[1] - (int)(Math.sin(angle) * obstacleDiameter * 2) + (int)(Math.sin(angle + 45) * obstacleDiameter / 2) );
                        gc.lineTo(temp[0] - (int)(Math.cos(angle) * obstacleDiameter * 2), temp[1] - (int)(Math.sin(angle) * obstacleDiameter * 2));
                        gc.stroke();
                        gc.fillOval(temp[0] - (int)(obstacleDiameter/2), temp[1] - (int)(obstacleDiameter/2), obstacleDiameter, obstacleDiameter);

                    }

                }
            }
        }.start();


        primaryStage.show();
    }
}
