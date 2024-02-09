package com.modsen.cabaggregator.paymentservice.service;

import com.modsen.cabaggregator.paymentservice.dto.CardRequest;
import com.modsen.cabaggregator.paymentservice.dto.CustomerRequest;
import com.stripe.model.Balance;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentIntentConfirmParams;

import java.util.Map;

public interface StripeModelsBuilderService {
    Charge createCharge(Map<String, Object> params);

    Token createToken(CardRequest request);

    Customer createCustomer(CustomerRequest request);

    Customer retrieveCustomer(String customerId);

    Balance retrieveBalance();

    PaymentIntent createPaymentIntent(Map<String, Object> params);

    void confirmPaymentIntent(PaymentIntent intent, PaymentIntentConfirmParams params);

    void createPaymentMethodWithAttachment(Map<String, Object> params, Map<String, Object> attachments);

    void updateCustomerBalance(Customer customer, CustomerUpdateParams params);
}
