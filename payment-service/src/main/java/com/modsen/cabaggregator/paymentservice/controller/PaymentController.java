package com.modsen.cabaggregator.paymentservice.controller;

import com.modsen.cabaggregator.paymentservice.dto.BalanceResponse;
import com.modsen.cabaggregator.paymentservice.dto.CardRequest;
import com.modsen.cabaggregator.paymentservice.dto.ChargeRequest;
import com.modsen.cabaggregator.paymentservice.dto.ChargeResponse;
import com.modsen.cabaggregator.paymentservice.dto.CustomerChargeRequest;
import com.modsen.cabaggregator.paymentservice.dto.CustomerRequest;
import com.modsen.cabaggregator.paymentservice.dto.CustomerResponse;
import com.modsen.cabaggregator.paymentservice.dto.MessageResponse;
import com.modsen.cabaggregator.paymentservice.dto.TokenResponse;
import com.modsen.cabaggregator.paymentservice.service.PaymentService;
import com.modsen.cabaggregator.paymentservice.util.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.PAYMENTS_ENDPOINT)
@Tag(name = "Payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/charge")
    public MessageResponse chargeCard(@RequestBody @Valid ChargeRequest chargeRequest) {
        return paymentService.charge(chargeRequest);
    }

    @PostMapping("/token")
    public TokenResponse createToken(@RequestBody @Valid CardRequest request) {
        return paymentService.createToken(request);
    }

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@RequestBody @Valid CustomerRequest request) {
        return paymentService.createCustomer(request);
    }

    @GetMapping("/customers/{id}")
    public CustomerResponse findCustomer(@PathVariable Long id) {
        return paymentService.retrieveCustomer(id);
    }

    @GetMapping("/balance")
    public BalanceResponse getBalance() {
        return paymentService.getBalance();
    }

    @PostMapping("/customers/charge")
    public ChargeResponse chargeFromCustomer(@RequestBody @Valid CustomerChargeRequest request) {
        return paymentService.chargeFromCustomer(request);
    }

}
