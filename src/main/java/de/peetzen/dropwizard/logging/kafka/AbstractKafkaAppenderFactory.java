package de.peetzen.dropwizard.logging.kafka;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import com.github.danielwegener.logback.kafka.KafkaAppender;
import io.dropwizard.logging.common.AbstractAppenderFactory;
import io.dropwizard.logging.common.AppenderFactory;
import io.dropwizard.logging.common.async.AsyncAppenderFactory;
import io.dropwizard.logging.common.filter.FilterFactory;
import io.dropwizard.logging.common.filter.LevelFilterFactory;
import io.dropwizard.logging.common.layout.LayoutFactory;

/**
 * A base implementation of {@link AppenderFactory} producing an appender based on {@link KafkaAppender}.
 * <p>
 * Based on {@link io.dropwizard.logging.common.AbstractOutputStreamAppenderFactory}
 */
public abstract class AbstractKafkaAppenderFactory<E extends DeferredProcessingAware> extends AbstractAppenderFactory<E> {

    protected abstract KafkaAppender<E> appender(LoggerContext context);

    @Override
    public Appender<E> build(LoggerContext context, String applicationName, LayoutFactory<E> layoutFactory,
                             LevelFilterFactory<E> levelFilterFactory, AsyncAppenderFactory<E> asyncAppenderFactory) {
        KafkaAppender<E> appender = appender(context);
        LayoutWrappingEncoder<E> layoutEncoder = new LayoutWrappingEncoder<>();
        layoutEncoder.setLayout(buildLayout(context, layoutFactory));
        appender.setEncoder(layoutEncoder);

        appender.addFilter(levelFilterFactory.build(threshold));
        getFilterFactories().stream().map(FilterFactory::build).forEach(appender::addFilter);
        appender.start();
        return wrapAsync(appender, asyncAppenderFactory);
    }
}
