package com.akeir.restaurant.config;

public final class FeatureFlags {

    private static final String PROPERTY_NFE_MOCK_ENABLED = "restaurant.feature.nfe.mock.enabled";
    private static final String PROPERTY_NFE_PROVIDER = "restaurant.nfe.provider";

    private FeatureFlags() {
    }

    public static boolean isNFeMockEnabled() {
        return Boolean.parseBoolean(System.getProperty(PROPERTY_NFE_MOCK_ENABLED, "true"));
    }

    public static String getConfiguredNFeProvider() {
        String provider = System.getProperty(PROPERTY_NFE_PROVIDER, "");
        if (provider == null) {
            return "";
        }
        return provider.trim();
    }
}
