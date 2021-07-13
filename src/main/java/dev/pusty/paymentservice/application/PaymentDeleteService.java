package dev.pusty.paymentservice.application;

import dev.pusty.paymentservice.application.ports.PaymentEntity;
import dev.pusty.paymentservice.application.ports.PaymentRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

class PaymentDeleteService {
    private final PaymentRepository paymentRepository;

    PaymentDeleteService(final PaymentRepository paymentRepository) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository);
    }

    public Optional<PaymentEntity> delete(final PaymentId id) {
        return paymentRepository.delete(id)
                .map(Function.identity());
    }
}
