package dev.pusty.paymentservice.infrastructure.persistence;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties("payments.persistence")
@Data
@Validated
public class PersistenceProperties {
    @NotNull
    PersistenceStorageEngine storageEngine;
}
