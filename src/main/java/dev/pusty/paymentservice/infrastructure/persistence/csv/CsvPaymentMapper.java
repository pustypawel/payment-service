package dev.pusty.paymentservice.infrastructure.persistence.csv;

import dev.pusty.paymentservice.application.EditablePayment;
import dev.pusty.paymentservice.application.Payment;
import dev.pusty.paymentservice.application.PaymentId;
import org.javamoney.moneta.Money;

public class CsvPaymentMapper {
    public CsvPaymentEntity map(final Payment payment) {
        return new CsvPaymentEntity(payment.id().value(),
                payment.getMoney().getNumber(),
                payment.getMoney().getCurrency(),
                payment.getUserId(),
                payment.getTargetBankAccountNumber());
    }

    public Payment map(final CsvPaymentEntity csvPayment) {
        return new EditablePayment(PaymentId.of(csvPayment.getId()),
                csvPayment.getUserId(),
                Money.of(csvPayment.getAmount(), csvPayment.getCurrency()),
                csvPayment.getTargetBankAccountNumber());
    }
}
