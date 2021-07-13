package dev.pusty.paymentservice.application;

import dev.pusty.paymentservice.api.UpdatePayment;
import dev.pusty.paymentservice.application.ports.PaymentEntity;
import dev.pusty.paymentservice.application.ports.PaymentRepository;

import java.util.Objects;
import java.util.Optional;

class PaymentUpdateService {
    private final PaymentRepository paymentRepository;

    public PaymentUpdateService(final PaymentRepository paymentRepository) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository);
    }

    public Optional<PaymentEntity> update(final PaymentId paymentId,
                                          final UpdatePayment updatePayment) {
        return paymentRepository.load(paymentId)
                .map(payment -> payment.update(updatePayment))
                .map(paymentRepository::save);
    }
}
