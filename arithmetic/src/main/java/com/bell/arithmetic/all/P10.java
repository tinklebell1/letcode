package com.bell.arithmetic.all;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class P10 {
    public boolean isMatch(String s, String p) {
        return false;
        }

    public static void main(String[] args) {
        BigDecimal bigDecimal = BigDecimal.valueOf(99.995).setScale(2, RoundingMode.HALF_DOWN);
        System.out.println(bigDecimal);
        BigDecimal bigDecimal1 = BigDecimal.valueOf(99.99501).setScale(2, RoundingMode.HALF_DOWN);
        System.out.println(bigDecimal1);
    }
}
