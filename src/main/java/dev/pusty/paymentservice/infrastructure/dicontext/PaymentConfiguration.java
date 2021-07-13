package dev.pusty.paymentservice.infrastructure.dicontext;

import dev.pusty.paymentservice.application.PaymentFacade;
import dev.pusty.paymentservice.application.PaymentModule;
import dev.pusty.paymentservice.application.ports.PaymentQueryService;
import dev.pusty.paymentservice.application.ports.PaymentRepository;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(PaymentQueryService.class)
public class PaymentConfiguration {
    private final PaymentQueryService paymentQueryService;
    private final PaymentRepository paymentRepository;

    public PaymentConfiguration(final PaymentQueryService paymentQueryService,
                                final PaymentRepository paymentRepository) {
        this.paymentQueryService = paymentQueryService;
        this.paymentRepository = paymentRepository;
    }

    @Bean
    public PaymentFacade paymentFacade() {
        final PaymentModule paymentModule = PaymentModule.create(paymentQueryService, paymentRepository);
        return paymentModule.getPaymentFacade();
    }
}
