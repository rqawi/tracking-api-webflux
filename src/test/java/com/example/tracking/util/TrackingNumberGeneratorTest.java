package com.example.tracking.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class TrackingNumberGeneratorTest {

    @Test
    public void testGenerate_trackingNumberFormat() {
        String origin = "NY";
        String destination = "LA";
        String slug = "abc123";

        String trackingNumber = TrackingNumberGenerator.generate(origin, destination, slug);

        // The tracking number should not be null or empty
        assertNotNull(trackingNumber);
        assertFalse(trackingNumber.isEmpty());

        // Check length constraint: max 16 characters
        assertTrue(trackingNumber.length() <= 16);

        // The tracking number should be uppercase alphanumeric
        assertTrue(trackingNumber.matches("[A-Z0-9]+"));

        // It should start with origin + destination + slug (cleaned, uppercase, no special chars)
        String expectedBase = (origin + destination + slug).replaceAll("[^A-Z0-9]", "").toUpperCase();

        // The tracking number starts with at least the base prefix (or a substring if too long)
        assertTrue(trackingNumber.startsWith(expectedBase.substring(0, Math.min(expectedBase.length(), trackingNumber.length()))));

        // The date prefix should be included right after the base part
        String datePrefix = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        assertTrue(trackingNumber.contains(datePrefix));

        // The suffix should be a valid uppercase hex number (0-9, A-F)
        String suffix = trackingNumber.substring(expectedBase.length() + datePrefix.length());
        assertTrue(suffix.matches("[0-9A-F]*"));
    }
}
