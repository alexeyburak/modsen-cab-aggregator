package com.modsen.cabaggregator.paymentservice.controller;

import com.modsen.cabaggregator.paymentservice.dto.ChargeRequest;
import com.modsen.cabaggregator.paymentservice.dto.ChargeResponse;
import com.modsen.cabaggregator.paymentservice.dto.CustomerChargeRequest;
import com.modsen.cabaggregator.paymentservice.dto.MessageResponse;
import com.modsen.cabaggregator.paymentservice.service.PaymentService;
import com.modsen.cabaggregator.paymentservice.util.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.RIDE_PAYMENTS_ENDPOINT)
@Tag(name = Constants.PAYMENTS)
public class RidePaymentController {

    private final PaymentService paymentService;

    @PostMapping(Constants.CHARGE_MAPPING)
    public MessageResponse chargeByCard(@RequestBody @Valid ChargeRequest chargeRequest) {
        return paymentService.charge(chargeRequest);
    }

    @PostMapping(Constants.CUSTOMERS_CHARGE_MAPPING)
    public ChargeResponse chargeFromCustomer(@RequestBody @Valid CustomerChargeRequest request) {
        return paymentService.chargeFromCustomer(request);
    }

}
