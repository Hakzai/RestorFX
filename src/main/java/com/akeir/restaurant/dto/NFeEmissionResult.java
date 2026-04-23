package com.akeir.restaurant.dto;

public class NFeEmissionResult {

    private final String status;
    private final String accessKey;
    private final String protocol;
    private final String xml;

    public NFeEmissionResult(String status, String accessKey, String protocol, String xml) {
        this.status = status;
        this.accessKey = accessKey;
        this.protocol = protocol;
        this.xml = xml;
    }

    public String getStatus() {
        return status;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getXml() {
        return xml;
    }
}