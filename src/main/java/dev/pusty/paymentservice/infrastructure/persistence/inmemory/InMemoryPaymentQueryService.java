package dev.pusty.paymentservice.infrastructure.persistence.inmemory;

import dev.pusty.paymentservice.api.PaymentResource;
import dev.pusty.paymentservice.application.Payment;
import dev.pusty.paymentservice.application.PaymentId;
import dev.pusty.paymentservice.application.ports.PaymentQueryService;
import dev.pusty.paymentservice.infrastructure.rest.PaymentResourceMapper;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class InMemoryPaymentQueryService implements PaymentQueryService {
    private final PaymentResourceMapper paymentResourceMapper;
    private final ConcurrentMap<PaymentId, Payment> payments;

    public InMemoryPaymentQueryService(final PaymentResourceMapper paymentResourceMapper,
                                       final ConcurrentMap<PaymentId, Payment> payments) {
        this.paymentResourceMapper = Objects.requireNonNull(paymentResourceMapper);
        this.payments = Objects.requireNonNull(payments);
    }

    @Override
    public Optional<PaymentResource> getById(final PaymentId id) {
        return Optional.ofNullable(payments.get(id))
                .map(paymentResourceMapper::map);
    }

    @Override
    public Collection<PaymentResource> getAll() {
        return payments.values()
                .stream()
                .map(paymentResourceMapper::map)
                .collect(Collectors.toSet());
    }
}
