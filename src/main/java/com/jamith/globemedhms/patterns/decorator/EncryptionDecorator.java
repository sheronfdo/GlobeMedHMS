package com.jamith.globemedhms.patterns.decorator;

public class EncryptionDecorator {
    private static final int SHIFT = 3; // Simple Caesar cipher shift for demonstration

    public static String encrypt(String text) {
        if (text == null) return null;
        StringBuilder encrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            encrypted.append((char) (c + SHIFT));
        }
        return encrypted.toString();
    }

    public static String decrypt(String text) {
        if (text == null) return null;
        StringBuilder decrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            decrypted.append((char) (c - SHIFT));
        }
        return decrypted.toString();
    }
}