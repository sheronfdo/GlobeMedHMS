package com.jamith.globemedhms.patterns.decorator;

public class SanitizationDecorator {
    public static String sanitize(String input) {
        if (input == null) return null;
        return input.replaceAll("[<>\"&%;]", "")
                    .replaceAll("\\s+", " ")
                    .trim();
    }
}