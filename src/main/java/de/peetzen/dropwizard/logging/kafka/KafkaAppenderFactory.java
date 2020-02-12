package de.peetzen.dropwizard.logging.kafka;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.github.danielwegener.logback.kafka.KafkaAppender;
import com.github.danielwegener.logback.kafka.delivery.AsynchronousDeliveryStrategy;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * An {@link io.dropwizard.logging.AppenderFactory} implementation which provides an appender that writes events to a
 * Kafka topic.
 */
@JsonTypeName("kafka")
public class KafkaAppenderFactory extends AbstractKafkaAppenderFactory<ILoggingEvent> {

    @Valid
    @NotNull
    private List<String> bootstrapServers;

    @NotNull
    private String topic;

    private KafkaSecurityConfiguration security;

    @Override
    protected KafkaAppender<ILoggingEvent> appender(LoggerContext context) {
        KafkaAppender<ILoggingEvent> appender = new KafkaAppender<>();
        appender.setContext(context);
        appender.setDeliveryStrategy(new AsynchronousDeliveryStrategy());

        // exclude logs from org.apache.kafka.* to not end in an endless loop forwarding logs written by the
        // actual kafka forwarding implementation
        appender.addFilter(new IgnoreApacheKafkaLoggerFilter());

        appender.addProducerConfigValue("bootstrap.servers", bootstrapServers.stream().collect(Collectors.joining(",")));
        appender.setTopic(topic);

        if (security != null) {
            configure(appender, "security.protocol", security.getSecurityProtocol());
            configure(appender, "sasl.mechanism", security.getSaslMechanism());
            configure(appender, "sasl.jaas.config", security.getSaslJaas());

            configure(appender, "ssl.protocol", security.getSslProtocol());
            configure(appender, "ssl.enabled.protocols", security.getSslEnabledProtocols());
            configure(appender, "ssl.endpoint.identification.algorithm", security.getSslEndpointIdentificationAlgorithm());

            configure(appender, "ssl.truststore.location", security.getSslTruststoreLocation());
            configure(appender, "ssl.truststore.password", security.getSslTruststorePassword());
            configure(appender, "ssl.keystore.location", security.getSslKeystoreLocation());
            configure(appender, "ssl.keystore.password", security.getSslKeystorePassword());
        }
        return appender;
    }

    private void configure(KafkaAppender<ILoggingEvent> appender, String key, String value) {
        if (value != null) {
            appender.addProducerConfigValue(key, value);
        }
    }

    @JsonProperty
    public List<String> getBootstrapServers() {
        return bootstrapServers;
    }

    @JsonProperty
    public String getTopic() {
        return topic;
    }

    @JsonProperty
    public KafkaSecurityConfiguration getSecurity() {
        return security;
    }
}
