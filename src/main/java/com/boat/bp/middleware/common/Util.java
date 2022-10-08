package com.boat.bp.middleware.common;

import java.security.SecureRandom;

public class Util {

    private static final String AB =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom rnd = new SecureRandom();
    private static final int ERROR_ID_LENGTH = 8;


    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static String errorId() {
        return randomString(ERROR_ID_LENGTH);
    }
}
