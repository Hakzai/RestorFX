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

        return "Provider: " + provider.name() + "\n"
            + "Environment: " + environment + "\n"
            + "Certificate: " + certificatePath + "\n"
            + "Password: configured\n\n"
            + "Next step: wire the concrete real provider adapter behind NFeService.";
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
