package dev.pusty.paymentservice.infrastructure.persistence.csv;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import dev.pusty.paymentservice.application.Payment;
import dev.pusty.paymentservice.application.PaymentId;
import dev.pusty.paymentservice.application.ports.PaymentQueryService;
import dev.pusty.paymentservice.application.ports.PaymentRepository;
import dev.pusty.paymentservice.infrastructure.persistence.inmemory.InMemoryPaymentQueryService;
import dev.pusty.paymentservice.infrastructure.persistence.inmemory.InMemoryPaymentRepository;
import dev.pusty.paymentservice.infrastructure.rest.PaymentResourceMapper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.jackson.datatype.money.MoneyModule;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "payments.persistence.storage-engine", havingValue = "CSV")
@EnableConfigurationProperties(CsvPaymentRepositoryProperties.class)
public class CsvPaymentRepositoryConfiguration implements DisposableBean {
    private final CsvPaymentRepositoryProperties properties;
    private final ConcurrentMap<PaymentId, Payment> payments = new ConcurrentHashMap<>();
    private final PaymentResourceMapper paymentResourceMapper;
    private final Path storageFilePath;
    private final CsvMapper csvMapper;

    public CsvPaymentRepositoryConfiguration(final CsvPaymentRepositoryProperties properties,
                                             final PaymentResourceMapper paymentResourceMapper) {
        this.properties = properties;
        this.paymentResourceMapper = paymentResourceMapper;
        this.storageFilePath = Paths.get(properties.getFilePath());
        this.csvMapper = csvMapper();   //limitation - we cannot chose csv parser library
        initFile();
    }

    private void initFile() {
        try {
            Files.createFile(storageFilePath);
        } catch (final FileAlreadyExistsException e) {
            //ignore
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (properties.getCleanUpOnExit()) {
            Files.deleteIfExists(storageFilePath);
        }
    }

    //TODO probably it can be better configured
    CsvMapper csvMapper() {
        return CsvMapper.csvBuilder()
                .addModule(new MoneyModule())
                .build();
    }

    @Bean
    public PaymentRepository csvPaymentRepository() {
        final CsvPaymentStorage csvPaymentStorage = new CsvPaymentStorage(new CsvPaymentMapper(), csvMapper, storageFilePath);
        final Collection<Payment> loadedPayments = csvPaymentStorage.read();
        loadedPayments.forEach(loadedPayment -> payments.put(loadedPayment.id(), loadedPayment));
        final InMemoryPaymentRepository inMemoryPaymentRepository = new InMemoryPaymentRepository(payments);
        return new CsvPaymentRepositoryDecorator(inMemoryPaymentRepository, csvPaymentStorage);
    }

    @Bean
    public PaymentQueryService paymentQueryService() {
        return new InMemoryPaymentQueryService(paymentResourceMapper, payments);
    }
}
