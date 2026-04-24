package com.akeir.restaurant.integration.nfe;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

import javax.net.ssl.SSLContext;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akeir.restaurant.dto.NFeEmissionRequest;
import com.akeir.restaurant.dto.NFeEmissionResult;

public class RealNFeService implements NFeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealNFeService.class);
    private static final AtomicLong SEQUENCE = new AtomicLong(700000000000L);
    private static final String PROPERTY_ENDPOINT = "restaurant.nfe.real.endpoint";
    private static final String PROPERTY_TIMEOUT_MS = "restaurant.nfe.real.timeout.ms";
    private static final String PROPERTY_MAX_RETRIES = "restaurant.nfe.real.max-retries";
    private static final String PROPERTY_CERTIFICATE_PATH = "restaurant.nfe.real.certificate.path";
    private static final String PROPERTY_CERTIFICATE_PASSWORD = "restaurant.nfe.real.certificate.password";

    @Override
    public NFeEmissionResult emit(NFeEmissionRequest request) {
        validateRequest(request);

        String endpoint = requiredProperty(PROPERTY_ENDPOINT, "NFe real endpoint is not configured");
        int timeoutMs = integerProperty(PROPERTY_TIMEOUT_MS, 5000);
        int maxRetries = integerProperty(PROPERTY_MAX_RETRIES, 2);

        String issueDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String accessKey = generateAccessKey();
        String protocol = "REAL-" + SEQUENCE.incrementAndGet();
        String requestXml = buildProviderXml(request, issueDate, accessKey, protocol);

        try (CloseableHttpClient client = buildHttpClient(endpoint, timeoutMs, maxRetries)) {
            HttpPost post = new HttpPost(endpoint);
            post.setHeader("Accept", "application/xml,text/xml,*/*");
            post.setHeader("X-NFe-Provider", "RESTORFX");
            post.setEntity(new StringEntity(requestXml, ContentType.APPLICATION_XML));

            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = readEntity(response.getEntity());

                if (statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES) {
                    LOGGER.info("Real provider emission authorized with status {} and access key {}", statusCode, accessKey);
                    String finalXml = responseBody == null || responseBody.trim().isEmpty() ? requestXml : responseBody;
                    return new NFeEmissionResult("AUTHORIZED_REAL", accessKey, protocol, finalXml);
                }

                LOGGER.error("Real provider rejected emission (status: {}, access key: {})", statusCode, accessKey);
                throw new RuntimeException("Real provider rejected emission (HTTP " + statusCode + ")");
            }
        } catch (IOException exception) {
            LOGGER.error("Failed to emit NFe with real provider after retry flow", exception);
            throw new RuntimeException("Failed to emit NFe with real provider", exception);
        }
    }

    private void validateRequest(NFeEmissionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("NFe request cannot be null");
        }

        if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required for NFe emission");
        }

        if (request.getTotalCents() <= 0) {
            throw new IllegalArgumentException("NFe total amount must be greater than zero");
        }
    }

    private String generateAccessKey() {
        String number = String.valueOf(SEQUENCE.incrementAndGet());
        return leftPad(number, 44);
    }

    private String leftPad(String value, int length) {
        StringBuilder builder = new StringBuilder(value == null ? "" : value);
        while (builder.length() < length) {
            builder.insert(0, '0');
        }
        if (builder.length() > length) {
            return builder.substring(builder.length() - length);
        }
        return builder.toString();
    }

    private CloseableHttpClient buildHttpClient(String endpoint, int timeoutMs, int maxRetries) {
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(timeoutMs)
            .setConnectionRequestTimeout(timeoutMs)
            .setSocketTimeout(timeoutMs)
            .build();

        ServiceUnavailableRetryStrategy retryStrategy = new ServiceUnavailableRetryStrategy() {
            @Override
            public boolean retryRequest(org.apache.http.HttpResponse response, int executionCount, HttpContext context) {
                if (executionCount > maxRetries) {
                    return false;
                }
                int statusCode = response.getStatusLine().getStatusCode();
                return statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR
                    || statusCode == HttpStatus.SC_BAD_GATEWAY
                    || statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE
                    || statusCode == HttpStatus.SC_GATEWAY_TIMEOUT;
            }

            @Override
            public long getRetryInterval() {
                return 300L;
            }
        };

        if (endpoint.startsWith("https://")) {
            SSLContext sslContext = buildSslContextFromCertificate();
            return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(maxRetries, true))
                .setServiceUnavailableRetryStrategy(retryStrategy)
                .setSSLContext(sslContext)
                .build();
        }

        return HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .setRetryHandler(new DefaultHttpRequestRetryHandler(maxRetries, true))
            .setServiceUnavailableRetryStrategy(retryStrategy)
            .build();
    }

    private SSLContext buildSslContextFromCertificate() {
        String certificatePath = requiredProperty(PROPERTY_CERTIFICATE_PATH, "NFe certificate path is not configured");
        String certificatePassword = requiredProperty(PROPERTY_CERTIFICATE_PASSWORD, "NFe certificate password is not configured");

        Path path = Paths.get(certificatePath);
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new IllegalArgumentException("NFe certificate file does not exist");
        }

        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (java.io.InputStream inputStream = Files.newInputStream(path)) {
                keyStore.load(inputStream, certificatePassword.toCharArray());
            }

            return SSLContextBuilder.create()
                .loadKeyMaterial(keyStore, certificatePassword.toCharArray())
                .build();
        } catch (Exception exception) {
            throw new IllegalArgumentException("Failed to load certificate for HTTPS provider", exception);
        }
    }

    private String requiredProperty(String property, String message) {
        String value = System.getProperty(property);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private int integerProperty(String property, int defaultValue) {
        String value = System.getProperty(property);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed < 0) {
                throw new IllegalArgumentException("Property " + property + " cannot be negative");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Property " + property + " must be an integer");
        }
    }

    private String readEntity(HttpEntity entity) throws IOException {
        if (entity == null) {
            return "";
        }
        return EntityUtils.toString(entity, StandardCharsets.UTF_8);
    }

    private String buildProviderXml(NFeEmissionRequest request, String issueDate, String accessKey, String protocol) {
        StringBuilder xml = new StringBuilder();
        xml.append("<nfeProc version=\"4.00\" provider=\"real_http\">\n");
        xml.append("  <NFe>\n");
        xml.append("    <infNFe Id=\"NFe").append(accessKey).append("\">\n");
        xml.append("      <ide><dhEmi>").append(issueDate).append("</dhEmi></ide>\n");
        xml.append("      <emit><xNome>Restaurant Management Real Provider Adapter</xNome></emit>\n");
        xml.append("      <dest>\n");
        xml.append("        <xNome>").append(escapeXml(request.getCustomerName())).append("</xNome>\n");
        if (request.getCustomerDocument() != null && !request.getCustomerDocument().trim().isEmpty()) {
            xml.append("        <CPF>").append(escapeXml(request.getCustomerDocument())).append("</CPF>\n");
        }
        xml.append("      </dest>\n");
        xml.append("      <total><vNF>").append(formatCents(request.getTotalCents())).append("</vNF></total>\n");
        xml.append("      <infAdic><infCpl>Real provider emission request</infCpl></infAdic>\n");
        if (request.getNotes() != null && !request.getNotes().trim().isEmpty()) {
            xml.append("      <infCpl>").append(escapeXml(request.getNotes())).append("</infCpl>\n");
        }
        xml.append("    </infNFe>\n");
        xml.append("  </NFe>\n");
        xml.append("  <protNFe><nProt>").append(protocol).append("</nProt></protNFe>\n");
        xml.append("</nfeProc>");
        return xml.toString();
    }

    private String formatCents(int cents) {
        return BigDecimal.valueOf(cents, 2).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String escapeXml(String value) {
        return StringEscapeUtils.escapeXml11(value);
    }
}
