package com.modsen.cabaggregator.paymentservice.service;

import com.modsen.cabaggregator.paymentservice.dto.BalanceResponse;
import com.modsen.cabaggregator.paymentservice.dto.CardRequest;
import com.modsen.cabaggregator.paymentservice.dto.ChargeRequest;
import com.modsen.cabaggregator.paymentservice.dto.ChargeResponse;
import com.modsen.cabaggregator.paymentservice.dto.CustomerChargeRequest;
import com.modsen.cabaggregator.paymentservice.dto.CustomerRequest;
import com.modsen.cabaggregator.paymentservice.dto.CustomerResponse;
import com.modsen.cabaggregator.paymentservice.dto.MessageResponse;
import com.modsen.cabaggregator.paymentservice.dto.TokenResponse;

import java.util.UUID;

public interface PaymentService {
    MessageResponse charge(ChargeRequest request);

    TokenResponse createToken(CardRequest request);

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse retrieveCustomer(UUID id);

    BalanceResponse getBalance();

    ChargeResponse chargeFromCustomer(CustomerChargeRequest request);

    void createPaymentMethod(String customerId);
}
