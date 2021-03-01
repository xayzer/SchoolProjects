package com.example.testsub;

import java.util.ArrayList;

public class getFuncUtil {
    public static double getA(Double[] x, Double[] y) {
        int n = x.length;
        return (n * pSum(x, y) - sum(x) * sum(y)) / (n * sqSum(x) - Math
                .pow(sum(x), 2));
    }

    /*
     * 功能：返回常量系数系数b 公式：b = sum( y ) / n - a sum( x ) / n
     */
    public static double getB(Double[] x, Double[] y) {
        int n = x.length;
        double a = getA(x, y);
        return sum(y) / n - a * sum(x) / n;
    }

    /*
     * 功能：求和
     */
    private static double sum(Double[] ds) {
        double s = 0;
        for (double d : ds) {
            s = s + d;
        }
        return s;
    }

    /*
     * 功能：求平方和
     */
    private static double sqSum(Double[] ds) {
        double s = 0;
        for (double d : ds) {
            s = s + Math.pow(d, 2);
        }
        return s;
    }

    /*
     * 功能：返回对应项相乘后的和
     */
    private static double pSum(Double[] x, Double[] y) {
        double s = 0;
        for (int i = 0; i < x.length; i++) {
            s = s + x[i] * y[i];
        }
        return s;
    }


    public static double ListDoubleArrayMax(ArrayList<Double> dataList) {
        double max = Double.MIN_VALUE;
        for (Double d : dataList) {
            if (d.doubleValue() > max) {
                max = d.doubleValue();
            }
        }
        return max;
    }

    public static double ListDoubleArrayMin(ArrayList<Double> dataList) {
        double min = Double.MAX_VALUE;
        for (Double d : dataList) {
            if (d.doubleValue() < min) {
                min = d.doubleValue();
            }
        }
        return min;
    }

}
