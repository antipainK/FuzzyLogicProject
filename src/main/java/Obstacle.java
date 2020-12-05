public class Obstacle {
    public double x, y;
    public double angle;
    public double speed;

    public Obstacle(double x, double y, double angle, double speed){
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.speed = speed;
    }

    public void move(){
        this.x += speed * Math.cos(Math.toRadians(angle));
        this.y += speed * Math.sin(Math.toRadians(angle));
    }
}
