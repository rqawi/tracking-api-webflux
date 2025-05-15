package com.example.tracking.util;

import java.util.UUID;

public class TrackingNumberGenerator {

    public static String generate(String origin, String destination, String slug) {
        String base = origin.toUpperCase() + destination.toUpperCase();
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return (base + random).toUpperCase().substring(0, Math.min(16, base.length() + 8));
    }
}
