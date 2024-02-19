package com.modsen.cabaggregator.notificationservice.service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.cabaggregator.notificationservice.dto.NotificationDto;
import com.modsen.cabaggregator.notificationservice.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaNotificationListener {

    public static final String DEFAULT_EMAIL_TEXT = "Email text";

    private final ObjectMapper mapper;
    private final NotificationSender sender;

    @KafkaListener(topics = {"start-ride-topic"}, groupId = "groupId-1")
    public void startRideListener(String notificationAsString) {
        NotificationDto notification = readNotificationDto(notificationAsString);
        if (notification == null) {
            return;
        }
        sender.send(
                notification.to(),
                "Ride was started",
                DEFAULT_EMAIL_TEXT
        );
    }

    @KafkaListener(topics = {"reject-ride-topic"}, groupId = "groupId-1")
    public void rejectRideListener(String notificationAsString) {
        NotificationDto notification = readNotificationDto(notificationAsString);
        if (notification == null) {
            return;
        }
        sender.send(
                notification.to(),
                "Ride was rejected",
                DEFAULT_EMAIL_TEXT
        );
    }

    @KafkaListener(topics = {"finish-ride-topic"}, groupId = "groupId-1")
    public void finishRideListener(String notificationAsString) {
        NotificationDto notification = readNotificationDto(notificationAsString);
        if (notification == null) {
            return;
        }
        sender.send(
                notification.to(),
                "Ride was finished",
                DEFAULT_EMAIL_TEXT
        );
    }

    @KafkaListener(topics = {"change-payment-status-topic"}, groupId = "groupId-1")
    public void changePaymentStatusListener(String notificationAsString) {
        NotificationDto notification = readNotificationDto(notificationAsString);
        if (notification == null) {
            return;
        }
        sender.send(
                notification.to(),
                "Ride was payed",
                DEFAULT_EMAIL_TEXT
        );
    }

    private NotificationDto readNotificationDto(String notificationAsString) {
        try {
            return mapper.readValue(notificationAsString, NotificationDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error processing value. {}", notificationAsString);
            return null;
        }
    }

}
