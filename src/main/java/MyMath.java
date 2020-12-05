public class MyMath {
    public static double distance(double x1, double y1, double x2, double y2){
        return Math.hypot(x1-x2, y1-y2);
    }

    public static double angle(double x1, double y1, double x2, double y2){
        return Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
    }
}
