package dev.pusty.paymentservice.infrastructure.persistence;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(PersistenceProperties.class)
public class PersistenceConfiguration {
}
