package com.jachaks.redisshortener.util;

import java.math.BigInteger;

public class Base62 {
    private static final String BASE62_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final BigInteger BASE = BigInteger.valueOf(62);

    public static String encode(BigInteger value) {
        StringBuilder result = new StringBuilder();
        while (value.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divideAndRemainder = value.divideAndRemainder(BASE);
            result.append(BASE62_ALPHABET.charAt(divideAndRemainder[1].intValue()));
            value = divideAndRemainder[0];
        }
        return result.reverse().toString();
    }
}
