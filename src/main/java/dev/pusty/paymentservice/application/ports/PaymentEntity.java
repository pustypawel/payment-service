package dev.pusty.paymentservice.application.ports;

import dev.pusty.paymentservice.application.PaymentId;

public interface PaymentEntity extends PaymentPojo {
    PaymentId id();
}
