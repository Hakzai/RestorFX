package com.akeir.restaurant.integration.nfe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.akeir.restaurant.dto.NFeEmissionRequest;
import com.akeir.restaurant.dto.NFeEmissionResult;

public class MockNFeServiceTest {

    @Test
    public void emitShouldReturnAuthorizedMockPayload() {
        MockNFeService service = new MockNFeService();
        NFeEmissionRequest request = new NFeEmissionRequest("Maria Silva", "12345678910", 14590, "Table 7 dinner");

        NFeEmissionResult result = service.emit(request);

        assertNotNull(result);
        assertEquals("AUTHORIZED_MOCK", result.getStatus());
        assertNotNull(result.getAccessKey());
        assertEquals(44, result.getAccessKey().length());
        assertTrue(result.getProtocol().startsWith("MOCK-"));
        assertTrue(result.getXml().contains("<nfeProc"));
        assertTrue(result.getXml().contains("<xNome>Maria Silva</xNome>"));
        assertTrue(result.getXml().contains("<vNF>145.90</vNF>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void emitShouldRejectNullRequest() {
        MockNFeService service = new MockNFeService();
        service.emit(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emitShouldRejectBlankCustomerName() {
        MockNFeService service = new MockNFeService();
        NFeEmissionRequest request = new NFeEmissionRequest("   ", "12345678910", 1000, null);
        service.emit(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emitShouldRejectNonPositiveTotal() {
        MockNFeService service = new MockNFeService();
        NFeEmissionRequest request = new NFeEmissionRequest("Maria Silva", "12345678910", 0, null);
        service.emit(request);
    }
}