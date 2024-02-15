package com.modsen.cabaggregator.notificationservice.service.impl;

import com.modsen.cabaggregator.notificationservice.service.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationSenderImpl implements NotificationSender {

    @Override
    public void send(String to, String subject, String text) {
        log.info("To: {}, Subject: {}, Text: {}", to, subject, text);
    }

}
