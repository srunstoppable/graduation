package com.example.graduation.util;

import java.text.DecimalFormat;

/**
 * @author s r
 * @date 2019/1/24
 */
public class Util {
    public static double getDemcial(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return Double.valueOf(decimalFormat.format(d));
    }
}
