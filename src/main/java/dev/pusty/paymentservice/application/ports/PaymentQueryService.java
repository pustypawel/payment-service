package dev.pusty.paymentservice.application.ports;

import dev.pusty.paymentservice.api.PaymentResource;
import dev.pusty.paymentservice.application.PaymentId;

import java.util.Collection;
import java.util.Optional;

public interface PaymentQueryService {
    Optional<PaymentResource> getById(PaymentId id);

    Collection<PaymentResource> getAll();
}
