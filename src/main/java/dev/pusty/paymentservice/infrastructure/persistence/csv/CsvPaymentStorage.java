package dev.pusty.paymentservice.infrastructure.persistence.csv;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import dev.pusty.paymentservice.application.Payment;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CsvPaymentStorage {
    private final CsvPaymentMapper csvPaymentMapper;
    private final ObjectReader csvReader;
    private final ObjectWriter csvWriter;
    private final Path storageFilePath;

    CsvPaymentStorage(final CsvPaymentMapper csvPaymentMapper,
                      final CsvMapper csvMapper,
                      final Path storageFilePath) {
        this.csvPaymentMapper = csvPaymentMapper;
        final CsvSchema schema = csvMapper.typedSchemaFor(CsvPaymentEntity.class)
                .withLineSeparator("");  // Files.write appends line separator
        this.csvReader = csvMapper.readerFor(CsvPaymentEntity.class).with(schema);
        this.csvWriter = csvMapper.writerFor(CsvPaymentEntity.class).with(schema);
        this.storageFilePath = storageFilePath;
    }

    Collection<Payment> read() {
        try (final Stream<String> lines = Files.lines(storageFilePath)) {
            return lines
                    .map(this::deserialize)
                    .map(csvPaymentMapper::map)
                    .collect(Collectors.toSet());
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    synchronized void update(final Collection<Payment> payments) {
        try {
            final Collection<String> csvPaymentsRecords = payments
                    .stream()
                    .map(csvPaymentMapper::map)
                    .map(this::serialize)
                    .collect(Collectors.toSet());
            Files.write(storageFilePath, csvPaymentsRecords);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String serialize(final CsvPaymentEntity csvPaymentEntity) {
        try {
            return csvWriter.writeValueAsString(csvPaymentEntity);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private CsvPaymentEntity deserialize(final String line) {
        try {
            return csvReader.readValue(line, CsvPaymentEntity.class);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
