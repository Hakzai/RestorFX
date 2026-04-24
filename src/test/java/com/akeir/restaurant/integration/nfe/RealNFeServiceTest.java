package com.akeir.restaurant.integration.nfe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Test;

import com.akeir.restaurant.dto.NFeEmissionRequest;
import com.akeir.restaurant.dto.NFeEmissionResult;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class RealNFeServiceTest {

    private static final String PROPERTY_ENDPOINT = "restaurant.nfe.real.endpoint";
    private static final String PROPERTY_TIMEOUT_MS = "restaurant.nfe.real.timeout.ms";
    private static final String PROPERTY_MAX_RETRIES = "restaurant.nfe.real.max-retries";

    @After
    public void tearDown() {
        System.clearProperty(PROPERTY_ENDPOINT);
        System.clearProperty(PROPERTY_TIMEOUT_MS);
        System.clearProperty(PROPERTY_MAX_RETRIES);
    }

    @Test
    public void emitShouldRetryAndReturnAuthorizedPayload() throws Exception {
        AtomicInteger attempts = new AtomicInteger(0);
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/emit", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                int currentAttempt = attempts.incrementAndGet();
                if (currentAttempt == 1) {
                    byte[] temporaryError = "temporary".getBytes("UTF-8");
                    exchange.sendResponseHeaders(503, temporaryError.length);
                    try (OutputStream outputStream = exchange.getResponseBody()) {
                        outputStream.write(temporaryError);
                    }
                    return;
                }

                byte[] response = "<nfeResponse status=\"authorized\"/>".getBytes("UTF-8");
                exchange.sendResponseHeaders(200, response.length);
                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(response);
                }
            }
        });
        server.start();
        try {
            int port = server.getAddress().getPort();
            System.setProperty(PROPERTY_ENDPOINT, "http://localhost:" + port + "/emit");
            System.setProperty(PROPERTY_TIMEOUT_MS, "3000");
            System.setProperty(PROPERTY_MAX_RETRIES, "2");

            RealNFeService service = new RealNFeService();
            NFeEmissionRequest request = new NFeEmissionRequest("Maria Silva", "12345678910", 14590, "Table 7 dinner");

            NFeEmissionResult result = service.emit(request);

            assertNotNull(result);
            assertEquals("AUTHORIZED_REAL", result.getStatus());
            assertNotNull(result.getAccessKey());
            assertEquals(44, result.getAccessKey().length());
            assertTrue(result.getProtocol().startsWith("REAL-"));
            assertTrue(result.getXml().contains("authorized"));
            assertEquals(2, attempts.get());
        } finally {
            server.stop(0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void emitShouldFailWhenEndpointIsMissing() {
        System.clearProperty(PROPERTY_ENDPOINT);

        RealNFeService service = new RealNFeService();
        NFeEmissionRequest request = new NFeEmissionRequest("Maria Silva", "12345678910", 14590, "Table 7 dinner");
        service.emit(request);
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
