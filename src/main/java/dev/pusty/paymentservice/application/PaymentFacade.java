package dev.pusty.paymentservice.application;

import dev.pusty.paymentservice.api.CreatePayment;
import dev.pusty.paymentservice.api.PaymentResource;
import dev.pusty.paymentservice.api.UpdatePayment;
import dev.pusty.paymentservice.application.ports.PaymentEntity;
import dev.pusty.paymentservice.application.ports.PaymentQueryService;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class PaymentFacade {
    private final PaymentQueryService queryService;
    private final PaymentCreateService createService;
    private final PaymentUpdateService updateService;
    private final PaymentDeleteService deleteService;

    PaymentFacade(final PaymentQueryService queryService,
                  final PaymentCreateService createService,
                  final PaymentUpdateService updateService,
                  final PaymentDeleteService deleteService) {
        this.queryService = Objects.requireNonNull(queryService);
        this.createService = Objects.requireNonNull(createService);
        this.updateService = Objects.requireNonNull(updateService);
        this.deleteService = Objects.requireNonNull(deleteService);
    }

    public Collection<PaymentResource> getAll() {
        return queryService.getAll();
    }

    public Optional<PaymentResource> getById(final PaymentId paymentId) {
        return queryService.getById(paymentId);
    }

    public PaymentEntity create(final CreatePayment createPayment) {
        return createService.create(createPayment);
    }

    public Optional<PaymentEntity> update(final PaymentId paymentId,
                                          final UpdatePayment updatePayment) {
        return updateService.update(paymentId, updatePayment);
    }

    public Optional<PaymentEntity> delete(final PaymentId paymentId) {
        return deleteService.delete(paymentId);
    }
}
