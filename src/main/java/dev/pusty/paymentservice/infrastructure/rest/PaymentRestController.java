package dev.pusty.paymentservice.infrastructure.rest;

import dev.pusty.paymentservice.api.CreatePayment;
import dev.pusty.paymentservice.api.PaymentResource;
import dev.pusty.paymentservice.api.UpdatePayment;
import dev.pusty.paymentservice.application.PaymentFacade;
import dev.pusty.paymentservice.application.PaymentId;
import dev.pusty.paymentservice.application.ports.PaymentEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("/api/payments")
public class PaymentRestController {
    private final PaymentFacade paymentFacade;
    private final PaymentResourceMapper paymentResourceMapper;

    public PaymentRestController(final PaymentFacade paymentFacade,
                                 final PaymentResourceMapper paymentResourceMapper) {
        this.paymentFacade = Objects.requireNonNull(paymentFacade);
        this.paymentResourceMapper = Objects.requireNonNull(paymentResourceMapper);
    }

    private ResponseEntity<PaymentResource> notFound() {
        return ResponseEntity.notFound()
                .build();
    }

    @GetMapping
    public Collection<PaymentResource> getAll() {   //TODO pagination?
        return paymentFacade.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResource> getById(@PathVariable final String id) {
        return paymentFacade.getById(PaymentId.of(id))
                .map(ResponseEntity::ok)
                .orElseGet(this::notFound);
    }

    @PostMapping
    public PaymentResource create(@Valid @RequestBody final CreatePayment createPayment) {
        final PaymentEntity payment = paymentFacade.create(createPayment);
        return paymentResourceMapper.map(payment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResource> update(@PathVariable final String id,
                                                  @Valid @RequestBody final UpdatePayment updatePayment) {
        return paymentFacade.update(PaymentId.of(id), updatePayment)
                .map(paymentResourceMapper::map)
                .map(ResponseEntity::ok)
                .orElseGet(this::notFound);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentResource> delete(@PathVariable final String id) {
        return paymentFacade.delete(PaymentId.of(id))
                .map(paymentResourceMapper::map)
                .map(ResponseEntity::ok)
                .orElseGet(this::notFound);
    }

}
