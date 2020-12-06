public class MyMath {
    public static double distance(double x1, double y1, double x2, double y2){
        return Math.hypot(x1-x2, y1-y2);
    }

    public static double angle(double x1, double y1, double x2, double y2){
        if( x1 == x2 ){
            if( y2 < y1 ) return 270.0;
            else return 90.0;
        }
        else if( y1 == y2 ){
            if( x2 < x1 ) return 180.0;
            else return 0.0;
        }

        if( x2 < x1 && y2 < y1 ){
            return 270.0 - Math.toDegrees( Math.asin( Math.abs( x1 - x2 ) / MyMath.distance(x1,y1,x2,y2) ) );
        }
        if( x2 < x1 && y2 > y1 ){
            return 180.0 - Math.toDegrees( Math.asin( Math.abs( y1 - y2 ) / MyMath.distance(x1,y1,x2,y2) ) );
        }
        if( x2 > x1 && y2 > y1 ){
            return Math.toDegrees( Math.asin( Math.abs( y1 - y2 ) / MyMath.distance(x1,y1,x2,y2) ) );
        }
        if( x2 > x1 && y2 < y1 ){
            return 270.0 + Math.toDegrees( Math.asin( Math.abs( x1 - x2 ) / MyMath.distance(x1,y1,x2,y2) ) );
        }
        System.out.println("I shouldn't be here.");
        return 0.0;
    }

    public static int[] translate(double x, double y, double[] mapCords, double canvasWidth, double canvasHeight){
        int canvas_x = (int)(((x - mapCords[0]) / (mapCords[2] - mapCords[0])) * (-1 * canvasWidth) + canvasWidth);
        int canvas_y = (int)(((y - mapCords[1]) / (mapCords[3] - mapCords[1])) * (-1 * canvasHeight) + canvasHeight);
        return new int[]{canvas_x, canvas_y};
    }
}
