package dev.pusty.paymentservice.application;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

@EqualsAndHashCode
@ToString
public class UserId {
    private final String value;

    private UserId(final String value) {
        if (Objects.requireNonNull(value).isBlank()) {
            throw new IllegalArgumentException("value cannot be blank. value = " + value);
        }
        this.value = value;
    }

    public static UserId create(final String userId) {
        return new UserId(userId);
    }

    public String value() {
        return value;
    }
}
