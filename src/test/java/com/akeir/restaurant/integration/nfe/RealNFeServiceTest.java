package com.akeir.restaurant.integration.nfe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.akeir.restaurant.dto.NFeEmissionRequest;
import com.akeir.restaurant.dto.NFeEmissionResult;

public class RealNFeServiceTest {

    @Test
    public void emitShouldReturnPendingProviderPayload() {
        RealNFeService service = new RealNFeService();
        NFeEmissionRequest request = new NFeEmissionRequest("Maria Silva", "12345678910", 14590, "Table 7 dinner");

        NFeEmissionResult result = service.emit(request);

        assertNotNull(result);
        assertEquals("PENDING_REAL_PROVIDER", result.getStatus());
        assertNotNull(result.getAccessKey());
        assertEquals(44, result.getAccessKey().length());
        assertTrue(result.getProtocol().startsWith("REAL-STUB-"));
        assertTrue(result.getXml().contains("provider=\"real_stub\""));
        assertTrue(result.getXml().contains("<vNF>145.90</vNF>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void emitShouldRejectNullRequest() {
        RealNFeService service = new RealNFeService();
        service.emit(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emitShouldRejectBlankCustomerName() {
        RealNFeService service = new RealNFeService();
        NFeEmissionRequest request = new NFeEmissionRequest("   ", "12345678910", 1000, null);
        service.emit(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emitShouldRejectNonPositiveTotal() {
        RealNFeService service = new RealNFeService();
        NFeEmissionRequest request = new NFeEmissionRequest("Maria Silva", "12345678910", 0, null);
        service.emit(request);
    }
}
