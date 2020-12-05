public class MyMath {
    public static double distance(double x1, double y1, double x2, double y2){
        return Math.hypot(x1-x2, y1-y2);
    }

    public static double angle(double x1, double y1, double x2, double y2){
        return (Math.toDegrees(Math.atan2(y2, x2) - Math.atan2(y1, x1)) + 360)%360;
        //TODO to nie działa
    }

    public static int[] translate(double x, double y, double[] mapCords, double canvasWidth, double canvasHeight){
        int canvas_x = (int)(((x - mapCords[0]) / (mapCords[2] - mapCords[0])) * (-1 * canvasWidth) + canvasWidth);
        int canvas_y = (int)(((y - mapCords[1]) / (mapCords[3] - mapCords[1])) * (-1 * canvasHeight) + canvasHeight);
        return new int[]{canvas_x, canvas_y};
    }
}
