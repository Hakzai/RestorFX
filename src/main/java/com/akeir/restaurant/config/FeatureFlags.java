package com.akeir.restaurant.config;

public final class FeatureFlags {

    private static final String PROPERTY_NFE_MOCK_ENABLED = "restaurant.feature.nfe.mock.enabled";

    private FeatureFlags() {
    }

    public static boolean isNFeMockEnabled() {
        return Boolean.parseBoolean(System.getProperty(PROPERTY_NFE_MOCK_ENABLED, "true"));
    }
}
