package dev.pusty.paymentservice.application;

import dev.pusty.paymentservice.PaymentSamples;
import dev.pusty.paymentservice.api.CreatePayment;
import dev.pusty.paymentservice.api.PaymentResource;
import dev.pusty.paymentservice.api.UpdatePayment;
import dev.pusty.paymentservice.application.ports.PaymentEntity;
import dev.pusty.paymentservice.infrastructure.persistence.inmemory.InMemoryPaymentRepositoryConfiguration;
import dev.pusty.paymentservice.infrastructure.rest.PaymentResourceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentFacadeTest {

    private PaymentFacade paymentFacade;

    private PaymentTestHelper paymentTestHelper;

    @BeforeEach
    public void setup() {
        final InMemoryPaymentRepositoryConfiguration repoConfiguration = new InMemoryPaymentRepositoryConfiguration(new PaymentResourceMapper());
        final PaymentModule paymentModule = PaymentModule.create(repoConfiguration.paymentQueryService(), repoConfiguration.paymentRepository());
        this.paymentFacade = paymentModule.getPaymentFacade();
        this.paymentTestHelper = new PaymentTestHelper(paymentFacade);
    }

    @Test
    public void should_create_payment() {
        //given:
        final CreatePayment paymentCreate = PaymentSamples.anyPaymentCreate();

        //when:
        final PaymentEntity payment = paymentFacade.create(paymentCreate);

        //then:
        assertNotNull(payment.id());
    }

    @Test
    public void should_update_payment() {
        //given:
        final PaymentId paymentId = paymentTestHelper.create().id();
        final UpdatePayment updatePayment = PaymentSamples.anyPaymentUpdate();

        //when:
        final Optional<PaymentEntity> updatedPaymentOptional = paymentFacade.update(paymentId, updatePayment);

        //then:
        assertTrue(updatedPaymentOptional.isPresent());
    }

    @Test
    public void should_get_existing_payment() {
        //given:
        final PaymentId paymentId = paymentTestHelper.create().id();

        //when:
        final Optional<PaymentResource> payment = paymentFacade.getById(paymentId);

        //then:
        assertTrue(payment.isPresent());
    }

    @Test
    public void should_not_get_not_existing_payment() {
        //given:
        final PaymentId notExistingPaymentId = PaymentId.of("NOT_EXISTING_PAYMENT_ID");

        //when:
        Optional<PaymentResource> payment = paymentFacade.getById(notExistingPaymentId);

        //then:
        assertFalse(payment.isPresent());
    }

    @Test
    public void should_delete_payment() {
        //given:
        final PaymentId paymentId = paymentTestHelper.create().id();

        //when:
        paymentFacade.delete(paymentId);
    }

    @Test
    public void should_not_get_deleted_payment() {
        //given:
        final PaymentId paymentId = paymentTestHelper.create().id();
        paymentFacade.delete(paymentId);

        //when:
        Optional<PaymentResource> payment = paymentFacade.getById(paymentId);

        //then:
        assertTrue(payment.isEmpty());
    }

    @Test
    public void should_get_all_payments() {
        //given:
        final int count = 10;
        paymentTestHelper.createTimes(count);

        //when:
        final Collection<PaymentResource> allPayments = paymentFacade.getAll();

        //then:
        assertEquals(count, allPayments.size());
    }

}