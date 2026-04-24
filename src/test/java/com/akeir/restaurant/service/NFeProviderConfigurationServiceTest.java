package com.akeir.restaurant.service;

import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;

import org.junit.Test;

import com.akeir.restaurant.dto.NFeProviderSetupRequest;

public class NFeProviderConfigurationServiceTest {

    private static final String PROPERTY_REAL_ENDPOINT = "restaurant.nfe.real.endpoint";
    private static final String PROPERTY_REAL_CERTIFICATE_PATH = "restaurant.nfe.real.certificate.path";
    private static final String PROPERTY_REAL_CERTIFICATE_PASSWORD = "restaurant.nfe.real.certificate.password";

    @Test
    public void validateAndBuildSummaryShouldAcceptValidPkcs12Setup() throws Exception {
        NFeProviderConfigurationService service = new NFeProviderConfigurationService();
        Path certificate = createPkcs12File("secret123");
        try {
            NFeProviderSetupRequest request = new NFeProviderSetupRequest(
                "REAL_PROVIDER",
                "homologation",
                certificate.toString(),
                "secret123"
            );

            String summary = service.validateAndBuildSummary(request, true);

            assertTrue(summary.contains("Provider: REAL_PROVIDER"));
            assertTrue(summary.contains("Environment: HOMOLOGATION"));
            assertTrue(summary.contains("Endpoint: https://homologacao.nfe.fazenda.gov.br/ws/nfeautorizacao4.asmx"));
            assertTrue(summary.contains("Password: configured"));
            assertTrue(certificate.toString().equals(System.getProperty(PROPERTY_REAL_CERTIFICATE_PATH)));
            assertTrue("secret123".equals(System.getProperty(PROPERTY_REAL_CERTIFICATE_PASSWORD)));
            assertTrue(System.getProperty(PROPERTY_REAL_ENDPOINT).contains("homologacao"));
        } finally {
            System.clearProperty(PROPERTY_REAL_ENDPOINT);
            System.clearProperty(PROPERTY_REAL_CERTIFICATE_PATH);
            System.clearProperty(PROPERTY_REAL_CERTIFICATE_PASSWORD);
            Files.deleteIfExists(certificate);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateAndBuildSummaryShouldRejectMissingCertificateFile() {
        NFeProviderConfigurationService service = new NFeProviderConfigurationService();
        NFeProviderSetupRequest request = new NFeProviderSetupRequest(
            "REAL_PROVIDER",
            "production",
            "target/test-db/non-existent-cert.p12",
            "secret123"
        );

        service.validateAndBuildSummary(request, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateAndBuildSummaryShouldRejectMockWhenFeatureFlagDisabled() throws Exception {
        NFeProviderConfigurationService service = new NFeProviderConfigurationService();
        Path certificate = createPkcs12File("secret123");
        try {
            NFeProviderSetupRequest request = new NFeProviderSetupRequest(
                "MOCK",
                "production",
                certificate.toString(),
                "secret123"
            );

            service.validateAndBuildSummary(request, false);
        } finally {
            Files.deleteIfExists(certificate);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateAndBuildSummaryShouldRejectInvalidEnvironment() throws Exception {
        NFeProviderConfigurationService service = new NFeProviderConfigurationService();
        Path certificate = createPkcs12File("secret123");
        try {
            NFeProviderSetupRequest request = new NFeProviderSetupRequest(
                "REAL_PROVIDER",
                "sandbox",
                certificate.toString(),
                "secret123"
            );

            service.validateAndBuildSummary(request, true);
        } finally {
            Files.deleteIfExists(certificate);
        }
    }

    private Path createPkcs12File(String password) throws Exception {
        Path directory = Paths.get("target", "test-db");
        Files.createDirectories(directory);
        Path path = Files.createTempFile(directory, "nfe-cert-", ".p12");

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, password.toCharArray());
        try (java.io.OutputStream outputStream = Files.newOutputStream(path)) {
            keyStore.store(outputStream, password.toCharArray());
        }

        return path;
    }
}
