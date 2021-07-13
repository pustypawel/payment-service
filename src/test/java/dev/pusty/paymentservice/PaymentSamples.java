package dev.pusty.paymentservice;

import dev.pusty.paymentservice.api.CreatePayment;
import dev.pusty.paymentservice.api.PaymentResource;
import dev.pusty.paymentservice.api.UpdatePayment;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import java.util.UUID;

public class PaymentSamples {
    public static PaymentResource anyPayment() {
        return new PaymentResource(anyPaymentId(),
                anyAmount(),
                anyUserId(),
                anyTargetBankAccountNumber());
    }

    public static UpdatePayment anyPaymentUpdate() {
        return new UpdatePayment(anyAmount().multiply(2),
                "UPDATED_" + anyUserId(),
                anyTargetBankAccountNumber());
    }

    private static String anyPaymentId() {
        return "paymentId";
    }

    public static String randomPaymentId() {
        return UUID.randomUUID().toString();
    }

    public static CreatePayment anyPaymentCreate() {
        return new CreatePayment(anyAmount(),
                anyUserId(),
                anyTargetBankAccountNumber());
    }

    private static MonetaryAmount anyAmount() {
        final MonetaryAmountFactory<?> moneyFactory = Monetary.getDefaultAmountFactory();
        moneyFactory.setNumber(100.58);
        moneyFactory.setCurrency("PLN");
        return moneyFactory.create();
    }

    private static String anyUserId() {
        return "userId";
    }

    private static String anyTargetBankAccountNumber() {
        return "20114020040000123456789012";
    }
}
