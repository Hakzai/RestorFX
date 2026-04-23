package com.akeir.restaurant.service;

import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;

import org.junit.Test;

import com.akeir.restaurant.dto.NFeProviderSetupRequest;

public class NFeProviderConfigurationServiceTest {

    @Test
    public void validateAndBuildSummaryShouldAcceptValidPkcs12Setup() throws Exception {
        NFeProviderConfigurationService service = new NFeProviderConfigurationService();
        Path certificate = createPkcs12File("secret123");
        try {
            NFeProviderSetupRequest request = new NFeProviderSetupRequest(
                "REAL_STUB",
                "homologation",
                certificate.toString(),
                "secret123"
            );

            String summary = service.validateAndBuildSummary(request, true);

            assertTrue(summary.contains("Provider: REAL_STUB"));
            assertTrue(summary.contains("Environment: HOMOLOGATION"));
            assertTrue(summary.contains("Password: configured"));
        } finally {
            Files.deleteIfExists(certificate);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateAndBuildSummaryShouldRejectMissingCertificateFile() {
        NFeProviderConfigurationService service = new NFeProviderConfigurationService();
        NFeProviderSetupRequest request = new NFeProviderSetupRequest(
            "REAL_STUB",
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
                "REAL_STUB",
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
