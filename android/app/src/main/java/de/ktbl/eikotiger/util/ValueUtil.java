package de.ktbl.eikotiger.util;

public class ValueUtil {

    public static boolean isFilled(String s){

        return !(s == null || s.isEmpty() || s.trim().isEmpty());
    }
}
