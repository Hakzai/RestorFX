package com.akeir.restaurant.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.akeir.restaurant.dto.NFeProviderSetupRequest;
import com.akeir.restaurant.integration.nfe.NFeProvider;
import com.akeir.restaurant.integration.nfe.NFeServiceFactory;

public class NFeProviderConfigurationService {

    private static final String PROPERTY_REAL_ENDPOINT = "restaurant.nfe.real.endpoint";
    private static final String PROPERTY_REAL_CERTIFICATE_PATH = "restaurant.nfe.real.certificate.path";
    private static final String PROPERTY_REAL_CERTIFICATE_PASSWORD = "restaurant.nfe.real.certificate.password";
    private static final String PROPERTY_REAL_TIMEOUT_MS = "restaurant.nfe.real.timeout.ms";
    private static final String PROPERTY_REAL_MAX_RETRIES = "restaurant.nfe.real.max-retries";

    private final Validator validator;

    public NFeProviderConfigurationService() {
        this(Validation.buildDefaultValidatorFactory().getValidator());
    }

    NFeProviderConfigurationService(Validator validator) {
        this.validator = validator;
    }

    public String validateAndBuildSummary(NFeProviderSetupRequest request, boolean nfeMockEnabled) {
        if (request == null) {
            throw new IllegalArgumentException("NFe setup request cannot be null");
        }

        Set<ConstraintViolation<NFeProviderSetupRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }

        String providerText = normalizeRequired(request.getProvider());
        NFeProvider provider = NFeServiceFactory.resolveProvider(providerText, nfeMockEnabled);
        String environment = normalizeRequired(request.getEnvironment()).toUpperCase();
        String certificatePath = normalizeRequired(request.getCertificatePath());
        String certificatePassword = normalizeRequired(request.getCertificatePassword());

        validateCertificate(certificatePath, certificatePassword);

        String endpoint = resolveEndpoint(provider, environment);
        applyRuntimeConfiguration(provider, endpoint, certificatePath, certificatePassword);

        return "Provider: " + provider.name() + "\n"
            + "Environment: " + environment + "\n"
            + "Endpoint: " + endpoint + "\n"
            + "Certificate: " + certificatePath + "\n"
            + "Password: configured\n\n"
            + "Next step: wire the concrete real provider adapter behind NFeService.";
    }

    private String resolveEndpoint(NFeProvider provider, String environment) {
        if (provider == NFeProvider.MOCK) {
            return "mock://local";
        }

        if ("HOMOLOGATION".equals(environment)) {
            return "https://homologacao.nfe.fazenda.gov.br/ws/nfeautorizacao4.asmx";
        }
        return "https://nfe.fazenda.gov.br/ws/nfeautorizacao4.asmx";
    }

    private void applyRuntimeConfiguration(NFeProvider provider, String endpoint, String certificatePath, String certificatePassword) {
        if (provider != NFeProvider.REAL_PROVIDER) {
            return;
        }

        System.setProperty(PROPERTY_REAL_ENDPOINT, endpoint);
        System.setProperty(PROPERTY_REAL_CERTIFICATE_PATH, certificatePath);
        System.setProperty(PROPERTY_REAL_CERTIFICATE_PASSWORD, certificatePassword);
        if (System.getProperty(PROPERTY_REAL_TIMEOUT_MS) == null) {
            System.setProperty(PROPERTY_REAL_TIMEOUT_MS, "5000");
        }
        if (System.getProperty(PROPERTY_REAL_MAX_RETRIES) == null) {
            System.setProperty(PROPERTY_REAL_MAX_RETRIES, "2");
        }
    }

    private void validateCertificate(String certificatePath, String certificatePassword) {
        Path path = Paths.get(certificatePath);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Certificate file does not exist");
        }

        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("Certificate path must be a file");
        }

        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream inputStream = Files.newInputStream(path)) {
                keyStore.load(inputStream, certificatePassword.toCharArray());
            }
        } catch (IllegalArgumentException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Failed to load certificate using provided password", exception);
        }
    }

    private String normalizeRequired(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
