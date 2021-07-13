package dev.pusty.paymentservice.application;

import dev.pusty.paymentservice.api.UpdatePayment;
import dev.pusty.paymentservice.application.ports.PaymentEntity;
import dev.pusty.paymentservice.application.ports.PaymentPojo;
import lombok.ToString;

import javax.money.MonetaryAmount;
import java.util.Objects;

@ToString
public abstract class Payment implements PaymentEntity {
    private final PaymentId id;
    private final UserId userId;
    private final MonetaryAmount money;
    private final BankAccountNumber targetBankAccountNumber;

    private Payment(final PaymentId id,
                    final UserId userId,
                    final MonetaryAmount money,
                    final BankAccountNumber targetBankAccountNumber) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId);
        this.money = Objects.requireNonNull(money);
        this.targetBankAccountNumber = Objects.requireNonNull(targetBankAccountNumber);
    }

    public Payment(final PaymentId id,
                   final String userId,
                   final MonetaryAmount money,
                   final String targetBankAccountNumber) {
        this(id, UserId.create(userId), money, BankAccountNumber.create(targetBankAccountNumber));
    }

    public Payment(final PaymentId id,
                   final PaymentPojo paymentPojo) {
        this(Objects.requireNonNull(id),
                paymentPojo.getUserId(),
                paymentPojo.getMoney(),
                paymentPojo.getTargetBankAccountNumber());
    }

    @Override
    public PaymentId id() {
        return id;
    }

    @Override
    public String getUserId() {
        return userId.value();
    }

    @Override
    public MonetaryAmount getMoney() {
        return money;
    }

    @Override
    public String getTargetBankAccountNumber() {
        return targetBankAccountNumber.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        //important! two created payments (before persistence process) are not equal!
        if (id == null && payment.id == null) return false;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isDraft() {
        return this instanceof DraftPayment;
    }

    public boolean isEditable() {
        return this instanceof EditablePayment;
    }

    public EditablePayment complete(final PaymentId id) {
        return requireState(DraftPayment.class).complete(id);
    }

    Payment update(final UpdatePayment updatePayment) {
        return requireState(EditablePayment.class).update(updatePayment);
    }

    private <TState extends Payment> TState requireState(Class<TState> requiredState) {
        if (requiredState.isInstance(this)) {
            return requiredState.cast(this);
        } else {
            throw new IllegalStateException("Payment is in illegal state. payment = " + this);
        }
    }
}
