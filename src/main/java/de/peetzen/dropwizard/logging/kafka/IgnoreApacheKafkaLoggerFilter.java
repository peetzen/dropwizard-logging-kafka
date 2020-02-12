package de.peetzen.dropwizard.logging.kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Logback filter that ignores all events coming from a logger with a name that starts with 'org.apache.kafka.'
 */
public class IgnoreApacheKafkaLoggerFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        String loggerName = event.getLoggerName();
        if (loggerName != null && loggerName.startsWith("org.apache.kafka.")) {
            return FilterReply.DENY;
        }
        return FilterReply.NEUTRAL;
    }
}