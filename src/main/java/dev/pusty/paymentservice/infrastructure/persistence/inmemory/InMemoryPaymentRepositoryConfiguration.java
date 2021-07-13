package dev.pusty.paymentservice.infrastructure.persistence.inmemory;

import dev.pusty.paymentservice.application.Payment;
import dev.pusty.paymentservice.application.PaymentId;
import dev.pusty.paymentservice.infrastructure.rest.PaymentResourceMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "payments.persistence.storage-engine", havingValue = "INMEMORY")
public class InMemoryPaymentRepositoryConfiguration {
    private final ConcurrentMap<PaymentId, Payment> payments = new ConcurrentHashMap<>();
    private final PaymentResourceMapper paymentResourceMapper;

    public InMemoryPaymentRepositoryConfiguration(final PaymentResourceMapper paymentResourceMapper) {
        this.paymentResourceMapper = Objects.requireNonNull(paymentResourceMapper);
    }

    @Bean
    public InMemoryPaymentRepository paymentRepository() {
        return new InMemoryPaymentRepository(payments);
    }

    @Bean
    public InMemoryPaymentQueryService paymentQueryService() {
        return new InMemoryPaymentQueryService(paymentResourceMapper, payments);
    }
}
