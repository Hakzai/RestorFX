package com.akeir.restaurant.integration.nfe;

import com.akeir.restaurant.config.FeatureFlags;

public final class NFeServiceFactory {

    private NFeServiceFactory() {
    }

    public static NFeProvider resolveDefaultProvider(boolean nfeMockEnabled) {
        String configuredProvider = FeatureFlags.getConfiguredNFeProvider();
        if (configuredProvider != null && !configuredProvider.isEmpty()) {
            return resolveProvider(configuredProvider, nfeMockEnabled);
        }

        if (nfeMockEnabled) {
            return NFeProvider.MOCK;
        }
        return NFeProvider.REAL_STUB;
    }

    public static NFeProvider resolveProvider(String providerValue, boolean nfeMockEnabled) {
        NFeProvider provider = NFeProvider.fromValue(providerValue);
        if (provider == NFeProvider.MOCK && !nfeMockEnabled) {
            throw new IllegalArgumentException("Mock provider is disabled by feature flag");
        }
        return provider;
    }

    public static NFeService create(NFeProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("NFe provider cannot be null");
        }

        switch (provider) {
            case MOCK:
                return new MockNFeService();
            case REAL_STUB:
                return new RealNFeService();
            default:
                throw new IllegalArgumentException("Unsupported NFe provider: " + provider);
        }
    }
}
