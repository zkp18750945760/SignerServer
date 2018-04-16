package utils;

public class MapUtils {
    // 地球半径
    private static final double EARTH_RADIUS = 6370996.81;

    // 弧度
    private static double radian(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算两个坐标点之间的距离
     *
     * @param lat1 纬度1
     * @param lng1 经度1
     * @param lat2 纬度2
     * @param lng2 经度2
     * @return void
     */
    public static double distanceOfTwoPoints(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = radian(lat1);
        double radLat2 = radian(lat2);
        double a = radLat1 - radLat2;
        double b = radian(lng1) - radian(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000;
        System.out.println("两点间的距离是：" + distance + "米");
        return distance;
    }
}
