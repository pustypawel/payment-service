package dev.pusty.paymentservice.application;

import dev.pusty.paymentservice.api.CreatePayment;

public final class DraftPayment extends Payment {
    public DraftPayment(final CreatePayment createPayment) {
        super(null,
                createPayment.getUserId(),
                createPayment.getMoney(),
                createPayment.getTargetBankAccountNumber());
    }

    public EditablePayment complete(final PaymentId id) {
        return new EditablePayment(id, this);
    }
}
