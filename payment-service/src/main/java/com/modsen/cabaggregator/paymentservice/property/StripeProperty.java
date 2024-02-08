package com.modsen.cabaggregator.paymentservice.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "stripe.keys")
public class StripeProperty {
    private String pk;
    private String sk;
}
