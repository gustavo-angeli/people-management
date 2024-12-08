package com.gusta.backend;

public class Utils {
    public static boolean isNumeric(String strNum) {
        if (strNum == null || strNum.equals("NaN")) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
