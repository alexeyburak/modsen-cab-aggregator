package com.modsen.cabaggregator.paymentservice.config;

import com.modsen.cabaggregator.paymentservice.client.CustomErrorDecoder;
import com.modsen.cabaggregator.paymentservice.client.CustomRetryer;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class AppConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:customMessages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new CustomRetryer();
    }

}
