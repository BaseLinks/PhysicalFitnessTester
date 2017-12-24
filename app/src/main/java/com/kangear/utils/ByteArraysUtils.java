package com.kangear.utils;

/**
 * Created by tony on 17-12-23.
 */

public class ByteArraysUtils {
    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x ", b));
        }
        return builder.toString();
    }
}
