package com.akeir.restaurant.integration.nfe;

public enum NFeProvider {

    MOCK,
    REAL_STUB;

    public static NFeProvider fromValue(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            throw new IllegalArgumentException("NFe provider is required");
        }

        String normalized = rawValue.trim().toUpperCase().replace('-', '_').replace(' ', '_');
        if ("REAL".equals(normalized)) {
            normalized = "REAL_STUB";
        }

        try {
            return NFeProvider.valueOf(normalized);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unsupported NFe provider: " + rawValue);
        }
    }
}
