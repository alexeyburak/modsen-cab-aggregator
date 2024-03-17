package com.modsen.cabaggregator.paymentservice.service.impl;

import com.modsen.cabaggregator.paymentservice.dto.BalanceResponse;
import com.modsen.cabaggregator.paymentservice.dto.CardRequest;
import com.modsen.cabaggregator.paymentservice.dto.ChargeRequest;
import com.modsen.cabaggregator.paymentservice.dto.ChargeResponse;
import com.modsen.cabaggregator.paymentservice.dto.CustomerChargeRequest;
import com.modsen.cabaggregator.paymentservice.dto.CustomerRequest;
import com.modsen.cabaggregator.paymentservice.dto.CustomerResponse;
import com.modsen.cabaggregator.paymentservice.dto.MessageResponse;
import com.modsen.cabaggregator.paymentservice.dto.RideInfoResponse;
import com.modsen.cabaggregator.paymentservice.dto.TokenResponse;
import com.modsen.cabaggregator.paymentservice.exception.CustomerIsAlreadyExistException;
import com.modsen.cabaggregator.paymentservice.exception.CustomerNotFoundException;
import com.modsen.cabaggregator.paymentservice.exception.InsufficientBalanceException;
import com.modsen.cabaggregator.paymentservice.model.PassengerCustomer;
import com.modsen.cabaggregator.paymentservice.repository.CustomerRepository;
import com.modsen.cabaggregator.paymentservice.service.PaymentService;
import com.modsen.cabaggregator.paymentservice.service.RideService;
import com.modsen.cabaggregator.paymentservice.service.StripeModelsBuilderService;
import com.modsen.cabaggregator.paymentservice.util.StripeParamsBuilder;
import com.stripe.model.Balance;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StripePaymentServiceImpl implements PaymentService {

    private final CustomerRepository customerRepository;
    private final StripeModelsBuilderService modelsBuilderService;
    private final RideService rideService;

    @Transactional
    @Override
    public MessageResponse charge(ChargeRequest request) {
        final UUID rideId = request.getRideId();
        Charge charge = modelsBuilderService.createCharge(
                StripeParamsBuilder.getChargeParams(
                        getRideById(rideId).getFinalCost().longValue(),
                        request.getCardToken()
                )
        );
        changeRideStatus(rideId);

        final String id = charge.getId();
        log.info("Charge by card token. Charge ID: {}", id);
        return new MessageResponse(
                String.format("Successful payment. ID: %s", id)
        );
    }

    @Override
    public TokenResponse getToken(CardRequest request) {
        Token token = modelsBuilderService.createToken(request);

        final String id = token.getId();
        log.info("Create Stripe token. ID: {}", id);
        return new TokenResponse(id);
    }

    @Transactional
    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        validateIsCustomerAlreadyExist(request);
        Customer customer = modelsBuilderService.createCustomer(request);
        final String id = customer.getId();

        createPaymentMethod(id);
        savePassengerCustomer(request.getPassengerId(), id);

        log.info("Create customer. ID: {}", id);
        return CustomerResponse.builder()
                .id(id)
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .description(customer.getDescription())
                .balance(customer.getBalance())
                .name(customer.getName())
                .build();
    }

    @Override
    public CustomerResponse retrieveCustomer(UUID passengerId) {
        Customer customer = modelsBuilderService.retrieveCustomer(
                getEntityById(passengerId).getCustomerId()
        );

        log.info("Retrieve passengers customer. ID: {}", passengerId);
        return CustomerResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .description(customer.getDescription())
                .balance(customer.getBalance())
                .name(customer.getName())
                .build();
    }

    @Override
    public BalanceResponse getBalance() {
        Balance balance = modelsBuilderService.retrieveBalance();

        return new BalanceResponse(
                BigDecimal.valueOf(balance.getPending().get(0).getAmount()),
                balance.getPending().get(0).getCurrency()
        );
    }

    @Transactional
    @Override
    public ChargeResponse chargeFromCustomer(CustomerChargeRequest request) {
        RideInfoResponse ride = getRideById(request.getRideId());
        final String customerId = getEntityById(ride.getPassenger().getId()).getCustomerId();
        final long amount = ride.getFinalCost().longValue();

        checkBalance(customerId, amount);

        PaymentIntent intent = modelsBuilderService.createPaymentIntent(
                StripeParamsBuilder.getPaymentIntentParams(amount, customerId)
        );
        intent.setPaymentMethod(customerId);
        modelsBuilderService.confirmPaymentIntent(
                intent,
                StripeParamsBuilder.getPaymentIntentConfirmParams()
        );

        updateBalance(customerId, amount);
        changeRideStatus(ride.getId());
        log.info("Charge from customer. Customer ID: {}", customerId);
        return ChargeResponse.builder()
                .amount(intent.getAmount())
                .id(intent.getId())
                .currency(intent.getCurrency())
                .build();
    }

    @Override
    public void createPaymentMethod(String customerId) {
        modelsBuilderService.createPaymentMethodWithAttachment(
                StripeParamsBuilder.getPaymentMethodParams(),
                StripeParamsBuilder.getPaymentMethodAttachParams(customerId)
        );
    }

    private RideInfoResponse getRideById(UUID rideId) {
        return rideService.getRideById(rideId);
    }

    private void changeRideStatus(UUID id) {
        rideService.changeStatus(id);
    }

    private void validateIsCustomerAlreadyExist(CustomerRequest request) {
        if (customerRepository.existsById(request.getPassengerId())) {
            throw new CustomerIsAlreadyExistException(request.getEmail());
        }
    }

    private void updateBalance(String customerId, Long amount) {
        Customer customer = modelsBuilderService.retrieveCustomer(customerId);
        modelsBuilderService.updateCustomerBalance(
                customer, StripeParamsBuilder.getCustomerUpdateParams(customer.getBalance() - amount)
        );
    }

    private void checkBalance(String customerId, Long amount) {
        Long balance = modelsBuilderService.retrieveCustomer(customerId).getBalance();
        if (balance < amount) {
            throw new InsufficientBalanceException(amount);
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
