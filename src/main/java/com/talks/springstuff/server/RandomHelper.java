package com.talks.springstuff.server;

import java.util.Random;

public class RandomHelper {

    public static String randomAlphaNumeric(int length) {
        Random random = new Random();
        return random.ints(48,122)
                .filter(i-> (i>=48 && i<=57) || (i>=65 && i<=90) || (i>=97 && i<=122))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public static String randomString(int length) {
        Random random = new Random();
        return random.ints(48,122)
                .filter(i-> (i>=65 && i<=90) || (i>=97 && i<=122))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
