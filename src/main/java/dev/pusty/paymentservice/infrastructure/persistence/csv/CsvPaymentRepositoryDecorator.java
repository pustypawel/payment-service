package dev.pusty.paymentservice.infrastructure.persistence.csv;

import dev.pusty.paymentservice.application.Payment;
import dev.pusty.paymentservice.application.PaymentId;
import dev.pusty.paymentservice.application.ports.PaymentRepository;
import dev.pusty.paymentservice.infrastructure.persistence.inmemory.InMemoryPaymentRepository;

import java.util.Objects;
import java.util.Optional;

public class CsvPaymentRepositoryDecorator implements PaymentRepository {
    private final InMemoryPaymentRepository inMemoryPaymentRepository;
    private final CsvPaymentStorage csvPaymentStorage;

    public CsvPaymentRepositoryDecorator(final InMemoryPaymentRepository inMemoryPaymentRepository,
                                         final CsvPaymentStorage csvPaymentStorage) {
        this.inMemoryPaymentRepository = Objects.requireNonNull(inMemoryPaymentRepository);
        this.csvPaymentStorage = Objects.requireNonNull(csvPaymentStorage);
    }

    @Override
    public Optional<Payment> load(final PaymentId paymentId) {
        return inMemoryPaymentRepository.load(paymentId);
    }

    @Override
    public Payment save(final Payment payment) {
        final Payment savedPayment = inMemoryPaymentRepository.save(payment);
        csvPaymentStorage.update(inMemoryPaymentRepository.getAll());
        return savedPayment;
    }

    @Override
    public Optional<Payment> delete(final PaymentId paymentId) {
        final Optional<Payment> deletedPayment = inMemoryPaymentRepository.delete(paymentId);
        deletedPayment.ifPresent(__ -> csvPaymentStorage.update(inMemoryPaymentRepository.getAll()));
        return deletedPayment;
    }
}
