package dev.pusty.paymentservice.application;

import dev.pusty.paymentservice.api.UpdatePayment;

import javax.money.MonetaryAmount;

public final class EditablePayment extends Payment {
    public EditablePayment(final PaymentId id,
                           final DraftPayment draftPayment) {
        super(id, draftPayment);
    }

    public EditablePayment(final PaymentId id,
                           final String userId,
                           final MonetaryAmount money,
                           final String targetBankAccountNumber) {
        super(id, userId, money, targetBankAccountNumber);
    }

    public EditablePayment update(final UpdatePayment updatePayment) {
        return new EditablePayment(id(),
                updatePayment.getUserId(),
                updatePayment.getMoney(),
                updatePayment.getTargetBankAccountNumber());
    }
}
