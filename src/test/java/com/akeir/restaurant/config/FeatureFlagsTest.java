package com.akeir.restaurant.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

public class FeatureFlagsTest {

    private static final String PROPERTY_NFE_MOCK_ENABLED = "restaurant.feature.nfe.mock.enabled";

    @After
    public void tearDown() {
        System.clearProperty(PROPERTY_NFE_MOCK_ENABLED);
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
}

