package de.peetzen.dropwizard.logging.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KafkaSecurityConfiguration {
    private String securityProtocol;
    private String saslMechanism;
    private String saslJaas;

    private String sslProtocol;
    private String sslEnabledProtocols;
    private String sslEndpointIdentificationAlgorithm;

    private String sslTruststoreLocation;
    private String sslTruststorePassword;
    private String sslKeystoreLocation;
    private String sslKeystorePassword;

    @JsonProperty
    public String getSecurityProtocol() {
        return securityProtocol;
    }

    @JsonProperty
    public String getSaslMechanism() {
        return saslMechanism;
    }

    @JsonProperty
    public String getSaslJaas() {
        return saslJaas;
    }

    @JsonProperty
    public String getSslProtocol() {
        return sslProtocol;
    }

    @JsonProperty
    public String getSslEnabledProtocols() {
        return sslEnabledProtocols;
    }

    @JsonProperty
    public String getSslEndpointIdentificationAlgorithm() {
        return sslEndpointIdentificationAlgorithm;
    }

    @JsonProperty
    public String getSslTruststoreLocation() {
        return sslTruststoreLocation;
    }

    @JsonProperty
    public String getSslTruststorePassword() {
        return sslTruststorePassword;
    }

    @JsonProperty
    public String getSslKeystoreLocation() {
        return sslKeystoreLocation;
    }

    @JsonProperty
    public String getSslKeystorePassword() {
        return sslKeystorePassword;
    }
}