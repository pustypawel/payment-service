package dev.pusty.paymentservice.infrastructure.rest;

import dev.pusty.paymentservice.api.PaymentResource;
import dev.pusty.paymentservice.application.PaymentId;
import dev.pusty.paymentservice.application.ports.PaymentEntity;
import dev.pusty.paymentservice.application.ports.PaymentPojo;
import org.springframework.stereotype.Component;

@Component
public class PaymentResourceMapper {
    public PaymentResource map(final PaymentEntity paymentEntity) {
        return map(paymentEntity.id(),
                paymentEntity);
    }

    public PaymentResource map(final PaymentId paymentId,
                               final PaymentPojo paymentPojo) {
        return new PaymentResource(paymentId.value(),
                paymentPojo.getMoney(),
                paymentPojo.getUserId(),
                paymentPojo.getTargetBankAccountNumber());
    }

}
