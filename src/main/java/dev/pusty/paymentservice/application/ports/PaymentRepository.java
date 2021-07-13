package dev.pusty.paymentservice.application.ports;

import dev.pusty.paymentservice.application.Payment;
import dev.pusty.paymentservice.application.PaymentId;

import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> load(PaymentId paymentId);

    Optional<Payment> delete(PaymentId paymentId);
}
