package dev.pusty.paymentservice.application.ports;

import javax.money.MonetaryAmount;

public interface PaymentPojo {
    MonetaryAmount getMoney();

    String getUserId();

    String getTargetBankAccountNumber();
}
