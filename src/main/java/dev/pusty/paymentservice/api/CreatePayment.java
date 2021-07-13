package dev.pusty.paymentservice.api;

import lombok.Value;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
public class CreatePayment {
    @NotNull
    MonetaryAmount money;
    @NotEmpty
    String userId;
    @NotEmpty
    String targetBankAccountNumber;
}
