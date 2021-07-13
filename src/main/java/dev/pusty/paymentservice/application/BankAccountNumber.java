package dev.pusty.paymentservice.application;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

@EqualsAndHashCode
@ToString
public class BankAccountNumber {
    private final String value;

    private BankAccountNumber(final String value) {
        if (Objects.requireNonNull(value).isBlank()) {
            throw new IllegalArgumentException("value cannot be blank. value = " + value);
        }
        this.value = value;
    }

    public static BankAccountNumber create(final String bankAccountName) {
        return new BankAccountNumber(bankAccountName);
    }

    public String value() {
        return value;
    }
}
