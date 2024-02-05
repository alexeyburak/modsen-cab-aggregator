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
import com.modsen.cabaggregator.paymentservice.exception.CustomerIsAlreadyExistException;
import com.modsen.cabaggregator.paymentservice.exception.CustomerNotFoundException;
import com.modsen.cabaggregator.paymentservice.exception.InsufficientBalanceException;
import com.modsen.cabaggregator.paymentservice.exception.StripeBalanceRetrieveException;
import com.modsen.cabaggregator.paymentservice.exception.StripeChargeException;
import com.modsen.cabaggregator.paymentservice.exception.StripeChargeFromCustomerException;
import com.modsen.cabaggregator.paymentservice.exception.StripeCheckBalanceException;
import com.modsen.cabaggregator.paymentservice.exception.StripeCustomerCreationException;
import com.modsen.cabaggregator.paymentservice.exception.StripeCustomerRetrieveException;
import com.modsen.cabaggregator.paymentservice.exception.StripePaymentMethodCreationException;
import com.modsen.cabaggregator.paymentservice.exception.StripeTokenCreationException;
import com.modsen.cabaggregator.paymentservice.exception.StripeUpdateBalanceException;
import com.modsen.cabaggregator.paymentservice.model.PassengerCustomer;
import com.modsen.cabaggregator.paymentservice.repository.CustomerRepository;
import com.modsen.cabaggregator.paymentservice.service.PaymentService;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StripePaymentServiceImpl implements PaymentService {

    private final CustomerRepository customerRepository;

    @Value("${stripe.keys.secret}")
    private String SECRET_KEY;

    @Value("${stripe.keys.public}")
    private String PUBLIC_KEY;

    @Override
    public MessageResponse charge(ChargeRequest request) {
        Stripe.apiKey = SECRET_KEY;

        try {
            Map<String, Object> params = StripeParamsBuilder.getChargeParams(request);
            Charge charge = Charge.create(params);

            final String id = charge.getId();
            log.info("Charge by card token. Charge ID: {}", id);
            return new MessageResponse(
                    String.format("Successful payment. ID: %s", id)
            );
        } catch (StripeException e) {
            throw new StripeChargeException(e);
        }
    }

    @Override
    public TokenResponse createToken(CardRequest request) {
        Stripe.apiKey = PUBLIC_KEY;

        try {
            Token token = Token.create(Map.of(
                    Constants.CARD, StripeParamsBuilder.getTokenCreationParams(request)
            ));

            final String id = token.getId();
            log.info("Create Stripe token. ID: {}", id);
            return new TokenResponse(id);
        } catch (StripeException e) {
            throw new StripeTokenCreationException(e);
        }
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        Stripe.apiKey = SECRET_KEY;

        validateIsCustomerAlreadyExist(request);
        try {
            Customer customer = Customer.create(
                    StripeParamsBuilder.getCustomerCreateParams(request)
            );
            final String id = customer.getId();

            createPaymentMethod(id);
            savePassengerCustomer(request.getPassengerId(), id);

            log.info("Create customer. ID: {}", id);
            return CustomerResponse.builder()
                    .id(id)
                    .email(customer.getEmail())
                    .phone(customer.getPhone())
                    .name(customer.getName())
                    .build();
        } catch (StripeException e) {
            throw new StripeCustomerCreationException(e);
        }
    }

    @Override
    public CustomerResponse retrieveCustomer(UUID passengerId) {
        Stripe.apiKey = SECRET_KEY;

        PassengerCustomer passengerCustomer = getEntityById(passengerId);
        try {
            Customer customer = Customer.retrieve(passengerCustomer.getCustomerId());

            log.info("Retrieve passengers customer. ID: {}", passengerId);
            return CustomerResponse.builder()
                    .id(customer.getId())
                    .email(customer.getEmail())
                    .phone(customer.getPhone())
                    .name(customer.getName())
                    .build();
        } catch (StripeException e) {
            throw new StripeCustomerRetrieveException(e);
        }
    }

    @Override
    public BalanceResponse getBalance() {
        Stripe.apiKey = SECRET_KEY;

        try {
            Balance balance = Balance.retrieve();

            return new BalanceResponse(
                    BigDecimal.valueOf(balance.getAvailable().get(0).getAmount()),
                    balance.getAvailable().get(0).getCurrency()
            );
        } catch (StripeException e) {
            throw new StripeBalanceRetrieveException(e);
        }
    }

    @Override
    public ChargeResponse chargeFromCustomer(CustomerChargeRequest request) {
        Stripe.apiKey = SECRET_KEY;

        final String customerId = getEntityById(request.getPassengerId()).getCustomerId();
        final long amount = request.getAmount().longValue();

        checkBalance(customerId, amount);
        updateBalance(customerId, amount);

        try {
            PaymentIntent intent = PaymentIntent.create(
                    StripeParamsBuilder.getPaymentIntentParams(amount, customerId)
            );
            intent.setPaymentMethod(customerId);
            intent.confirm(
                    StripeParamsBuilder.getPaymentIntentConfirmParams()
            );

            log.info("Charge from customer. Customer ID: {}", customerId);
            return ChargeResponse.builder()
                    .amount(intent.getAmount())
                    .id(intent.getId())
                    .currency(intent.getCurrency())
                    .build();
        } catch (StripeException e) {
            throw new StripeChargeFromCustomerException(e);
        }
    }

    @Override
    public void createPaymentMethod(String customerId) {
        Stripe.apiKey = SECRET_KEY;

        try {
            PaymentMethod paymentMethod = PaymentMethod.create(
                    StripeParamsBuilder.getPaymentMethodParams()
            );
            paymentMethod.attach(
                    StripeParamsBuilder.getPaymentMethodAttachParams(customerId)
            );
        } catch (StripeException e) {
            throw new StripePaymentMethodCreationException(e);
        }
    }

    private void validateIsCustomerAlreadyExist(CustomerRequest request) {
        if (customerRepository.existsById(request.getPassengerId())) {
            throw new CustomerIsAlreadyExistException(request.getEmail());
        }
    }

    private void updateBalance(String customerId, Long amount) {
        Stripe.apiKey = SECRET_KEY;

        try {
            Customer customer = Customer.retrieve(customerId);
            customer.update(
                    StripeParamsBuilder.getCustomerUpdateParams(customer.getBalance() - amount)
            );
        } catch (StripeException e) {
            throw new StripeUpdateBalanceException(e);
        }
    }

    private void checkBalance(String customerId, Long amount) {
        Stripe.apiKey = SECRET_KEY;

        try {
            Long balance = Customer.retrieve(customerId).getBalance();
            if (balance < amount) {
                throw new InsufficientBalanceException(amount);
            }
        } catch (StripeException e) {
            throw new StripeCheckBalanceException(e);
        }
    }

    private void savePassengerCustomer(UUID passengerId, String customerId) {
        log.info("Save passenger customer. Passenger ID: {}", passengerId);
        customerRepository.save(
                PassengerCustomer
                        .builder()
                        .customerId(customerId)
                        .passengerId(passengerId)
                        .build()
        );
    }

    private PassengerCustomer getEntityById(UUID passengerId) {
        return customerRepository.findById(passengerId)
                .orElseThrow(() -> new CustomerNotFoundException(passengerId));
    }

}
