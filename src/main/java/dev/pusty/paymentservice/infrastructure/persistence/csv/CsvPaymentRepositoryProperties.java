package dev.pusty.paymentservice.infrastructure.persistence.csv;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConfigurationProperties("payments.persistence.csv")
@Data
@Validated
public class CsvPaymentRepositoryProperties {
    @NotBlank
    private String filePath;
    @NotNull
    private Boolean cleanUpOnExit;
}
