package dev.pusty.paymentservice.infrastructure.persistence.inmemory;

import dev.pusty.paymentservice.application.Payment;
import dev.pusty.paymentservice.application.PaymentId;
import dev.pusty.paymentservice.application.ports.PaymentRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class InMemoryPaymentRepository implements PaymentRepository {
    private final ConcurrentMap<PaymentId, Payment> payments;

    public InMemoryPaymentRepository(final ConcurrentMap<PaymentId, Payment> payments) {
        this.payments = payments;
    }

    public Payment save(final Payment payment) {
        if (payment.isDraft()) {
            return doSave(payment.complete(PaymentId.generate()));
        } else {
            return doSave(payment);
        }
    }

    private Payment doSave(final Payment payment) {
        payments.put(payment.id(), payment);
        return payment;
    }

    @Override
    public Optional<Payment> load(final PaymentId paymentId) {
        return Optional.ofNullable(payments.get(paymentId));
    }

    @Override
    public Optional<Payment> delete(final PaymentId paymentId) {
        return Optional.ofNullable(payments.remove(paymentId));
    }

    public Collection<Payment> getAll() {
        return payments.values();
    }
}
