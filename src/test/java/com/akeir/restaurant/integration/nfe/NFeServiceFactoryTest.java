package com.akeir.restaurant.integration.nfe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

public class NFeServiceFactoryTest {

    private static final String PROPERTY_NFE_PROVIDER = "restaurant.nfe.provider";

    @After
    public void tearDown() {
        System.clearProperty(PROPERTY_NFE_PROVIDER);
    }

    @Test
    public void resolveDefaultProviderShouldUseMockWhenEnabledAndUnset() {
        System.clearProperty(PROPERTY_NFE_PROVIDER);

        NFeProvider provider = NFeServiceFactory.resolveDefaultProvider(true);

        assertEquals(NFeProvider.MOCK, provider);
    }

    @Test
    public void resolveDefaultProviderShouldUseRealStubWhenMockDisabled() {
        System.clearProperty(PROPERTY_NFE_PROVIDER);

        NFeProvider provider = NFeServiceFactory.resolveDefaultProvider(false);

        assertEquals(NFeProvider.REAL_STUB, provider);
    }

    @Test
    public void resolveDefaultProviderShouldHonorConfiguredProvider() {
        System.setProperty(PROPERTY_NFE_PROVIDER, "REAL_STUB");

        NFeProvider provider = NFeServiceFactory.resolveDefaultProvider(true);

        assertEquals(NFeProvider.REAL_STUB, provider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolveProviderShouldRejectMockWhenFlagDisabled() {
        NFeServiceFactory.resolveProvider("MOCK", false);
    }

    @Test
    public void createShouldReturnProviderImplementation() {
        NFeService mockService = NFeServiceFactory.create(NFeProvider.MOCK);
        NFeService realStubService = NFeServiceFactory.create(NFeProvider.REAL_STUB);

        assertTrue(mockService instanceof MockNFeService);
        assertTrue(realStubService instanceof RealNFeService);
    }
}
