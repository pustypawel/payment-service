package dev.pusty.paymentservice.infrastructure.persistence.csv;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

import javax.money.CurrencyUnit;

@Value
@JsonPropertyOrder(value = {"id", "amount", "currency", "userId", "targetBankAccountNumber"})
public class CsvPaymentEntity {
    String id;
    Number amount;
    CurrencyUnit currency;
    String userId;
    String targetBankAccountNumber;
}
