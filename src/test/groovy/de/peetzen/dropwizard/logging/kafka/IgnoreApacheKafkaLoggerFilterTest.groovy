package de.peetzen.dropwizard.logging.kafka

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.spi.FilterReply
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class IgnoreApacheKafkaLoggerFilterTest extends Specification {

    def event = Mock(ILoggingEvent)

    @Subject
    def filter = new IgnoreApacheKafkaLoggerFilter()

    @Unroll
    def "Decide for event with logger name #loggerName"() {
        given:
        event.getLoggerName() >> loggerName

        expect:
        filter.decide(event) == expectedFilterReply

        where:
        loggerName                                       || expectedFilterReply
        null                                             || FilterReply.NEUTRAL
        "some.other.logger"                              || FilterReply.NEUTRAL
        "org.apache.kafka.common.security.ssl.SomeClass" || FilterReply.DENY
    }
}
