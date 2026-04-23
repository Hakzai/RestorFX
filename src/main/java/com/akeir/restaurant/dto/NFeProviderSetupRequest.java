package com.akeir.restaurant.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class NFeProviderSetupRequest {

    @NotBlank(message = "Provider name is required for EPIC 7 setup")
    private String provider;

    @NotBlank(message = "Environment is required for EPIC 7 setup")
    @Pattern(regexp = "(?i)homologation|production", message = "Environment must be homologation or production")
    private String environment;

    @NotBlank(message = "Certificate path is required for EPIC 7 setup")
    private String certificatePath;

    @NotBlank(message = "Certificate password is required for EPIC 7 setup")
    private String certificatePassword;

    public NFeProviderSetupRequest(String provider, String environment, String certificatePath, String certificatePassword) {
        this.provider = provider;
        this.environment = environment;
        this.certificatePath = certificatePath;
        this.certificatePassword = certificatePassword;
    }

    public String getProvider() {
        return provider;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public String getCertificatePassword() {
        return certificatePassword;
    }
}
