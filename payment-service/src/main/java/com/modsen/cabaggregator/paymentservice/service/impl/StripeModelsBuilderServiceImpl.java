package com.modsen.cabaggregator.paymentservice.service.impl;

import com.modsen.cabaggregator.paymentservice.dto.CardRequest;
import com.modsen.cabaggregator.paymentservice.dto.CustomerRequest;
import com.modsen.cabaggregator.paymentservice.exception.StripeBalanceRetrieveException;
import com.modsen.cabaggregator.paymentservice.exception.StripeChargeException;
import com.modsen.cabaggregator.paymentservice.exception.StripeChargeFromCustomerException;
import com.modsen.cabaggregator.paymentservice.exception.StripeCustomerCreationException;
import com.modsen.cabaggregator.paymentservice.exception.StripeCustomerRetrieveException;
import com.modsen.cabaggregator.paymentservice.exception.StripePaymentMethodCreationException;
import com.modsen.cabaggregator.paymentservice.exception.StripeTokenCreationException;
import com.modsen.cabaggregator.paymentservice.exception.StripeUpdateBalanceException;
import com.modsen.cabaggregator.paymentservice.property.StripeProperty;
import com.modsen.cabaggregator.paymentservice.service.StripeModelsBuilderService;
import com.modsen.cabaggregator.paymentservice.util.Constants;
import com.modsen.cabaggregator.paymentservice.util.StripeParamsBuilder;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Balance;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Token;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentIntentConfirmParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StripeModelsBuilderServiceImpl implements StripeModelsBuilderService {

    private final StripeProperty property;

    @Override
    public Charge createCharge(Map<String, Object> params) {
        Stripe.apiKey = property.getSk();
        try {
            return Charge.create(params);
        } catch (StripeException e) {
            throw new StripeChargeException(e);
        }
    }

    @Override
    public Token createToken(CardRequest request) {
        Stripe.apiKey = property.getPk();
        try {
            return Token.create(Map.of(
                    Constants.CARD, StripeParamsBuilder.getTokenCreationParams(request)
            ));
        } catch (StripeException e) {
            throw new StripeTokenCreationException(e);
        }
    }

    @Override
    public Customer createCustomer(CustomerRequest request) {
        Stripe.apiKey = property.getSk();
        try {
            return Customer.create(
                    StripeParamsBuilder.getCustomerCreateParams(request)
            );
        } catch (StripeException e) {
            throw new StripeCustomerCreationException(e);
        }
    }

    @Override
    public Customer retrieveCustomer(String customerId) {
        Stripe.apiKey = property.getSk();
        try {
            return Customer.retrieve(customerId);
        } catch (StripeException e) {
            throw new StripeCustomerRetrieveException(e);
        }
    }

    @Override
    public Balance retrieveBalance() {
        Stripe.apiKey = property.getSk();
        try {
            return Balance.retrieve();
        } catch (StripeException e) {
            throw new StripeBalanceRetrieveException(e);
        }
    }

    @Override
    public PaymentIntent createPaymentIntent(Map<String, Object> params) {
        Stripe.apiKey = property.getSk();
        try {
            return PaymentIntent.create(params);
        } catch (StripeException e) {
            throw new StripeChargeFromCustomerException(e);
        }
    }

    @Override
    public void confirmPaymentIntent(PaymentIntent intent,
                                     PaymentIntentConfirmParams params) {
        Stripe.apiKey = property.getSk();
        try {
            intent.confirm(params);
        } catch (StripeException e) {
            throw new StripeChargeFromCustomerException(e);
        }
    }

    @Override
    public void createPaymentMethodWithAttachment(Map<String, Object> params,
                                                  Map<String, Object> attachments) {
        Stripe.apiKey = property.getSk();
        try {
            PaymentMethod paymentMethod = PaymentMethod.create(params);
            paymentMethod.attach(attachments);
        } catch (StripeException e) {
            throw new StripePaymentMethodCreationException(e);
        }
    }

    @Override
    public void updateCustomerBalance(Customer customer, CustomerUpdateParams params) {
        Stripe.apiKey = property.getSk();
        try {
            customer.update(params);
        } catch (StripeException e) {
            throw new StripeUpdateBalanceException(e);
        }
    }

}
