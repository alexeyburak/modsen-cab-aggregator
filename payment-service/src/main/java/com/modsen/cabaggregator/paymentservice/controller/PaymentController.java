package com.modsen.cabaggregator.paymentservice.controller;

import com.modsen.cabaggregator.paymentservice.dto.BalanceResponse;
import com.modsen.cabaggregator.paymentservice.dto.CardRequest;
import com.modsen.cabaggregator.paymentservice.dto.CustomerRequest;
import com.modsen.cabaggregator.paymentservice.dto.CustomerResponse;
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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.PAYMENTS_ENDPOINT)
@Tag(name = Constants.PAYMENTS)
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(Constants.TOKEN_MAPPING)
    public TokenResponse createToken(@RequestBody @Valid CardRequest request) {
        return paymentService.getToken(request);
    }

    @PostMapping(Constants.CUSTOMERS_MAPPING)
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@RequestBody @Valid CustomerRequest request) {
        return paymentService.createCustomer(request);
    }

    @GetMapping(Constants.CUSTOMERS_ID_MAPPING)
    public CustomerResponse findCustomer(@PathVariable UUID id) {
        return paymentService.retrieveCustomer(id);
    }

    @GetMapping(Constants.BALANCE_MAPPING)
    public BalanceResponse getBalance() {
        return paymentService.getBalance();
    }

}
