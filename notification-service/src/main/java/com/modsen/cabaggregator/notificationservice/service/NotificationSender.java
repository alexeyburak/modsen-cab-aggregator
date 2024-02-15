package com.modsen.cabaggregator.notificationservice.service;

public interface NotificationSender {

    void send(String to, String subject, String text);
}
