# Kafka Logging Appender for Dropwizard
[![CircleCI](https://img.shields.io/circleci/build/gh/peetzen/dropwizard-logging-kafka)](https://circleci.com/gh/peetzen/dropwizard-logging-kafka)
[![Maven Central](https://img.shields.io/maven-central/v/de.peetzen.dropwizard/dropwizard-logging-kafka)](https://search.maven.org/artifact/de.peetzen.dropwizard/dropwizard-logging-kafka)
[![License](https://img.shields.io/github/license/peetzen/dropwizard-logging-kafka)](http://www.apache.org/licenses/LICENSE-2.0.html)

[![Dropwizard](https://img.shields.io/badge/dropwizard-v1.x-green)](https://github.com/dropwizard/dropwizard)
[![Dropwizard](https://img.shields.io/badge/dropwizard-v1.3.x-green)](https://github.com/dropwizard/dropwizard)
[![Dropwizard](https://img.shields.io/badge/dropwizard-v2.x-green)](https://github.com/dropwizard/dropwizard)
[![Dropwizard](https://img.shields.io/badge/dropwizard-v3.x-green)](https://github.com/dropwizard/dropwizard)
[![Dropwizard](https://img.shields.io/badge/dropwizard-v4.x-green)](https://github.com/dropwizard/dropwizard)

Adds support to Dropwizard's logging factory to forward log events to a Kafka topic.

Using the Dropwizard application configuration file it is easy to customize the Kafka topic as well as connection parameters.

## Documentation
This library makes it easy to forward messages to a Kafka topic through Dropwizard's built-in logging capabilities using the common `org.slf4j.Logger` interface.

Using the standard configuration mechanisms of Dropwizard the Kafka topic as well as the connection settings can be configured in addition to the default appender settings. The settings can be specified in the applications configuration file.

Under the hood the latest version of [Apache Kafka Clients 2.4](https://www.apache.org/dist/kafka/2.4.0/RELEASE_NOTES.html) is being used to communicate to the Kafka brokers. To provide the logback appender integration the basic [logback-kafka-appender](https://github.com/danielwegener/logback-kafka-appender) library is being utilized.

## Getting started
The artifacts including source and binaries are available on the central Maven repositories.

For maven: 
```xml
<dependency>
  <groupId>de.peetzen.dropwizard</groupId>
  <artifactId>dropwizard-logging-kafka</artifactId>
  <version>4.0.0</version>
</dependency>
```

For gradle:
```yaml
runtimeOnly group: 'de.peetzen.dropwizard', name: 'dropwizard-logging-kafka', version: '4.0.0'
```

There is no need to have a compile time dependency. The library and the `kafka` appender functionality are auto discovered at runtime.

Due to breaking changes between Dropwizard versions different versions of this library are provided:

| Dropwizard Version  | Library Version | Comment                                              |
|---------------------|-----------------|------------------------------------------------------|
| `v4.x`              | `4.0.0`         | For latest Dropwizard version.                       |
| `v3.x`              | `3.0.0`         |                                                      |
| `v2.x`              | `2.0.0`         |                                                      |
| `v1.3.x`            | `1.3.0`         |                                                      |
| `v1.2.x` + `v1.1.x` | `1.0.0`         |                                                      |
| `< 1.1.0`           | `1.0.0`         | More recent version of `logback` libraries required. |

## Configuration Example
Specifying a logger appender with type `kafka` is all that is necessary. It can be added in addition to existing loggers and customized using the default settings, including the layout, filters end encoders.

```yaml
logging:
  level: WARN
  loggers:
    some.logger.name: DEBUG
  appenders:
    - type: console
    - type: kafka
      timeZone: UTC
      filterFactories:
        - type: my-custom-filter
      layout:
        type: logstash-json
        timestampFormat: "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
      topic: my.application.topic
      bootstrapServers:
        - kafka-1.my.company.org:1234
        - kafka-2.my.company.org:1234
      producerConfigs:
        security.protocol: sasl_ssl
        sasl.mechanism: SCRAM-SHA-512
        sasl.jaas.config: "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"<username>\" password=\"<password>\";"
```

## Configuration Reference

The default [Dropwizard logging configuration](https://www.dropwizard.io/en/stable/manual/configuration.html#logging) works and in addition the following new options are available:

| Name             | Description                                                                                                                                                                                            |
|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| topic            | The Kafka topic to use for the forwarded log events.                                                                                                                                                   |
| bootstrapServers | At least one Kafka server endpoint to connect to.                                                                                                                                                      |
| producerConfigs  | Optional `key: value` pairs for configuring the _Kafka Producer_. Detailed information can be found in the official [Kafka documentation](http://kafka.apache.org/documentation.html#producerconfigs). |

## Extension Support
The supported functionality is not enough, you want a more customized Kafka producer?

Do not hesitate to extend `AbstractKafkaAppenderFactory` and provide your own custom implementation.