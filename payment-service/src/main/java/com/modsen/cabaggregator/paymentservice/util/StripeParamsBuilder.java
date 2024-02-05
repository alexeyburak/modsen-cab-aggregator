package com.modsen.cabaggregator.paymentservice.util;

import com.modsen.cabaggregator.common.util.GlobalConstants;
import com.modsen.cabaggregator.paymentservice.dto.CardRequest;
import com.modsen.cabaggregator.paymentservice.dto.ChargeRequest;
import com.modsen.cabaggregator.paymentservice.dto.CustomerRequest;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentIntentConfirmParams;

import java.math.BigDecimal;
import java.util.Map;

public final class StripeParamsBuilder {

    private StripeParamsBuilder() {
    }

    public static Map<String, Object> getChargeParams(ChargeRequest request) {
        return Map.of(
                Constants.AMOUNT, request.getAmount(),
                Constants.CURRENCY, GlobalConstants.BYN,
                Constants.SOURCE, request.getCardToken()
        );
    }

    public static Map<String, Object> getTokenCreationParams(CardRequest request) {
        return Map.of(
                Constants.NUMBER, request.getCardNumber(),
                Constants.EXP_MONTH, request.getExpMonth(),
                Constants.EXP_YEAR, request.getExpYear(),
                Constants.CVC, request.getCvc()
        );
    }

    public static CustomerCreateParams getCustomerCreateParams(CustomerRequest request) {
        return CustomerCreateParams.builder()
                .setName(request.getName())
                .setEmail(request.getEmail())
                .setPhone(request.getPhone())
                .setBalance(request.getBalance().longValue())
                .build();
    }

    public static Map<String, Object> getPaymentIntentParams(Long amount, String customerId) {
        return Map.of(
                Constants.AMOUNT, amount,
                Constants.CURRENCY, GlobalConstants.BYN,
                Constants.CUSTOMER, customerId
        );
    }

    public static PaymentIntentConfirmParams getPaymentIntentConfirmParams() {
        return PaymentIntentConfirmParams.builder()
                .setPaymentMethod(Constants.PM_CARD_VISA)
                .build();
    }

    public static Map<String, Object> getPaymentMethodParams() {
        return Map.of(
                Constants.TYPE, Constants.CARD,
                Constants.CARD, Map.of(
                        Constants.TOKEN, Constants.TOK_VISA
                )
        );
    }

    public static Map<String, Object> getPaymentMethodAttachParams(String customerId) {
        return Map.of(
                Constants.CUSTOMER, customerId
        );
    }

    public static CustomerUpdateParams getCustomerUpdateParams(Long balance) {
        return CustomerUpdateParams.builder()
                .setBalance(balance)
                .build();
    }

}
