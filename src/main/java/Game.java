import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import model.Brain;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game extends Application{
    private static final int screenWidth = 1000;
    private static final int screenHeight = 1000;
    private static final double fps = 1;

    private static final int obstaclesAmount = 3;

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
        obstacles.add(new Obstacle(-10.0, -10.0, 45, 0.5));

        double mapStart_x = -10.0;
        double mapStart_y = -10.0;
        double mapEnd_x = 10.0;
        double mapEnd_y = 10.0;
        double outOfBoundsRange = 2.0;
        double[] mapCords = new double[]{mapStart_x, mapStart_y, mapEnd_x, mapEnd_y, outOfBoundsRange};

        Player player = new Player(mapCords, pathToDriver, obstacles, 0.5);
        root.getChildren().add(player.getAvatar() );

        long iterationsCounter = 0;
        AtomicBoolean gameOver = new AtomicBoolean(false);
        new AnimationTimer()
        {
            long startFrameTime = System.nanoTime();

            public void handle(long currentNanoTime)
            {
                int frameJump = (int) Math.floor((System.nanoTime() - startFrameTime) / (1000000000 / fps));
                if(frameJump > 1){
                    startFrameTime = System.nanoTime();

                }
            }
        }.start();

        while(gameOver.get() == false){
            long startFrameTime = System.nanoTime();

            while(obstacles.size() < obstaclesAmount){      // Create new obstacles if there isn't enough
                double randomPoint_x = Math.random() * (mapEnd_x - mapStart_x) + mapStart_x;
                double randomPoint_y;
                if(Math.random() < 0.5){
                    randomPoint_y = mapStart_y;
                }
                else {
                    randomPoint_y = mapEnd_y;
                }
                obstacles.add(new Obstacle(randomPoint_x, randomPoint_y, MyMath.angle(randomPoint_x, randomPoint_y, player.position_x, player.position_y), 0.5));
            }

            int i = 0;
            while(i < obstacles.size()){     // Move all obstacles
                Obstacle obstacle = obstacles.get(i);
                obstacle.move();
                if(obstacle.x > mapEnd_x + outOfBoundsRange  || obstacle.x < mapStart_x - outOfBoundsRange || obstacle.y > mapEnd_y + outOfBoundsRange || obstacle.y < mapStart_y - outOfBoundsRange){
                    obstacles.remove(obstacle);     // Remove obstacle, if out of the map
                }
                else{
                    i++;
                }
            }

            player.move();  // Move player

            //// DISPLAYING PART









            System.out.println("Gameloop: Iteration - " + (++iterationsCounter));


            long sleepTime = (long) (1000000000.0 / fps) - (System.nanoTime() - startFrameTime);
            if(sleepTime > 0){
                int milis = (int) (sleepTime / 1000000);
                int nanos = (int) (sleepTime - 1000000 * milis);
                Thread.sleep(milis, nanos);
            }
        }






        primaryStage.show();
    }

    private class gameLoop extends Thread{

    }
}
