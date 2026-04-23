package com.akeir.restaurant.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

public class FeatureFlagsTest {

    private static final String PROPERTY_NFE_MOCK_ENABLED = "restaurant.feature.nfe.mock.enabled";
    private static final String PROPERTY_NFE_PROVIDER = "restaurant.nfe.provider";

    @After
    public void tearDown() {
        System.clearProperty(PROPERTY_NFE_MOCK_ENABLED);
        System.clearProperty(PROPERTY_NFE_PROVIDER);
    }

    @Test
    public void isNFeMockEnabledShouldReturnTrueByDefault() {
        System.clearProperty(PROPERTY_NFE_MOCK_ENABLED);

        assertTrue(FeatureFlags.isNFeMockEnabled());
    }

    @Test
    public void isNFeMockEnabledShouldReturnFalseWhenPropertyIsFalse() {
        System.setProperty(PROPERTY_NFE_MOCK_ENABLED, "false");

        assertFalse(FeatureFlags.isNFeMockEnabled());
    }

    @Test
    public void isNFeMockEnabledShouldReturnTrueWhenPropertyIsTrue() {
        System.setProperty(PROPERTY_NFE_MOCK_ENABLED, "true");

        assertTrue(FeatureFlags.isNFeMockEnabled());
    }

    @Test
    public void getConfiguredNFeProviderShouldReturnTrimmedProperty() {
        System.setProperty(PROPERTY_NFE_PROVIDER, "  REAL_STUB  ");

        assertTrue("REAL_STUB".equals(FeatureFlags.getConfiguredNFeProvider()));
    }

    @Test
    public void getConfiguredNFeProviderShouldReturnEmptyWhenUnset() {
        System.clearProperty(PROPERTY_NFE_PROVIDER);

        assertTrue("".equals(FeatureFlags.getConfiguredNFeProvider()));
    }
}

