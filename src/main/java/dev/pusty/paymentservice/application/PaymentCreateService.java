package dev.pusty.paymentservice.application;

import dev.pusty.paymentservice.api.CreatePayment;
import dev.pusty.paymentservice.application.ports.PaymentEntity;
import dev.pusty.paymentservice.application.ports.PaymentRepository;

import java.util.Objects;

class PaymentCreateService {
    private final PaymentRepository paymentRepository;

    PaymentCreateService(final PaymentRepository paymentRepository) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository);
    }

    public PaymentEntity create(final CreatePayment createPayment) {
        final DraftPayment draftPayment = new DraftPayment(createPayment);
        return paymentRepository.save(draftPayment);
    }
}
