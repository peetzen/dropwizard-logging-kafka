package de.peetzen.dropwizard.logging.kafka;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.github.danielwegener.logback.kafka.KafkaAppender;
import com.github.danielwegener.logback.kafka.delivery.AsynchronousDeliveryStrategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * An {@link io.dropwizard.logging.common.AppenderFactory} implementation which provides an appender that writes events to a
 * Kafka topic.
 */
@JsonTypeName("kafka")
public class KafkaAppenderFactory extends AbstractKafkaAppenderFactory<ILoggingEvent> {

    @Valid
    @NotNull
    private List<String> bootstrapServers;

    @NotNull
    private String topic;

    /**
     * Configuration for the Kafka Producer. Detailed information can be found in the official Kafka documentation
     * http://kafka.apache.org/documentation.html#producerconfigs.
     */
    private Map<String, String> producerConfigs;

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
        if (producerConfigs != null) {
            applyConnectionConfiguration(appender);
        }
        return appender;
    }

    private void applyConnectionConfiguration(KafkaAppender<ILoggingEvent> appender) {
        producerConfigs.entrySet().stream()
            .filter(e -> e.getValue() != null)
            .forEach(e -> appender.addProducerConfigValue(e.getKey(), e.getValue()));
    }

    @JsonProperty
    public List<String> getBootstrapServers() {
        return bootstrapServers;
    }

    @JsonProperty
    public void setBootstrapServers(List<String> bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    @JsonProperty
    public String getTopic() {
        return topic;
    }

    @JsonProperty
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @JsonProperty
    public Map<String, String> getProducerConfigs() {
        return producerConfigs;
    }

    @JsonProperty
    public void setProducerConfigs(Map<String, String> producerConfigs) {
        this.producerConfigs = producerConfigs;
    }
}
