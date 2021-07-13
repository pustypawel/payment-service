package dev.pusty.paymentservice.application;

import dev.pusty.paymentservice.application.ports.PaymentQueryService;
import dev.pusty.paymentservice.application.ports.PaymentRepository;

import java.util.Objects;

public class PaymentModule {
    private final PaymentFacade paymentFacade;

    private PaymentModule(final PaymentFacade paymentFacade) {
        this.paymentFacade = Objects.requireNonNull(paymentFacade);
    }

    public PaymentFacade getPaymentFacade() {
        return paymentFacade;
    }

    public static PaymentModule create(final PaymentQueryService paymentQueryService,
                                       final PaymentRepository paymentRepository) {
        final PaymentCreateService paymentCreateService = new PaymentCreateService(paymentRepository);
        final PaymentUpdateService paymentUpdateService = new PaymentUpdateService(paymentRepository);
        final PaymentDeleteService paymentDeleteService = new PaymentDeleteService(paymentRepository);
        final PaymentFacade paymentFacade = new PaymentFacade(paymentQueryService,
                paymentCreateService,
                paymentUpdateService,
                paymentDeleteService);
        return new PaymentModule(paymentFacade);
    }
}
