package dev.pusty.paymentservice.application;

import dev.pusty.paymentservice.PaymentSamples;
import dev.pusty.paymentservice.api.CreatePayment;
import dev.pusty.paymentservice.application.ports.PaymentEntity;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PaymentTestHelper {
    private final PaymentFacade paymentFacade;

    PaymentTestHelper(final PaymentFacade paymentFacade) {
        this.paymentFacade = Objects.requireNonNull(paymentFacade);
    }

    Set<PaymentEntity> createTimes(int count) {
        return IntStream.range(0, count)
                .mapToObj(__ -> create())
                .collect(Collectors.toSet());
    }

    PaymentEntity create() {
        final CreatePayment paymentCreate = PaymentSamples.anyPaymentCreate();
        return paymentFacade.create(paymentCreate);
    }

    boolean isDeleted(final PaymentId paymentId) {
        return paymentFacade.getById(paymentId)
                .isEmpty();
    }
}
