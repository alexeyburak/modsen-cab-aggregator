package com.modsen.cabaggregator.paymentservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public final String PAYMENTS_ENDPOINT = "/api/v1/payments";
    public final String CHARGE_MAPPING = "/charge";
    public final String TOKEN_MAPPING = "/token";
    public final String CUSTOMERS_MAPPING = "/customers";
    public final String CUSTOMERS_ID_MAPPING = "/customers/{id}";
    public final String BALANCE_MAPPING = "/balance";
    public final String CUSTOMERS_CHARGE_MAPPING = "/customers/charge";
    public final String PAYMENTS = "Payments";
    public final String AMOUNT = "amount";
    public final String CURRENCY = "currency";
    public final String SOURCE = "source";
    public final String NUMBER = "number";
    public final String NEVER = "never";
    public final String EXP_MONTH = "exp_month";
    public final String EXP_YEAR = "exp_year";
    public final String CVC = "cvc";
    public final String CUSTOMER = "customer";
    public final String PM_CARD_VISA = "pm_card_visa";
    public final String TYPE = "type";
    public final String TOKEN = "token";
    public final String TOK_VISA = "tok_visa";
    public final String CARD = "card";
}
