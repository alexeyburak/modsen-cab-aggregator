package com.modsen.cabaggregator.paymentservice.service.impl;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StripePaymentServiceImpl implements PaymentService {

    @Value("${stripe.keys.secret}")
    private String SECRET_KEY;

    @Value("${stripe.keys.public}")
    private String PUBLIC_KEY;

    @Override
    public MessageResponse charge(ChargeRequest request) {
        return null;
    }

    @Override
    public TokenResponse createToken(CardRequest request) {
        return null;
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        return null;
    }

    @Override
    public CustomerResponse retrieveCustomer(Long id) {
        return null;
    }

    @Override
    public BalanceResponse getBalance() {
        return null;
    }

    @Override
    public ChargeResponse chargeFromCustomer(CustomerChargeRequest request) {
        return null;
    }

}
