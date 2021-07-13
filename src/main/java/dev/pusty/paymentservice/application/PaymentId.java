package dev.pusty.paymentservice.application;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@EqualsAndHashCode
@ToString
public class PaymentId {
    private final String value;

    private PaymentId(final String value) {
        if (Objects.requireNonNull(value).isBlank()) {
            throw new IllegalArgumentException("value cannot be blank. value = " + value);
        }
        this.value = Objects.requireNonNull(value);
    }

    public String value() {
        return value;
    }

    public static PaymentId of(final String id) {
        return new PaymentId(id);
    }

    public static PaymentId generate() {
        return of(UUID.randomUUID().toString());
    }
}
