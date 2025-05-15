package com.example.tracking.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class TrackingNumberGenerator {

    public static String generate(String origin, String destination, String slug) {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // e.g., 20250515
        String randomSuffix = Integer.toHexString(ThreadLocalRandom.current().nextInt(0x100000)).toUpperCase();
        String base = origin + destination + slug;
        String basePart = base.replaceAll("[^A-Z0-9]", "").toUpperCase();
        return (basePart + datePrefix + randomSuffix).substring(0, Math.min(16, basePart.length() + 13));
    }
}
