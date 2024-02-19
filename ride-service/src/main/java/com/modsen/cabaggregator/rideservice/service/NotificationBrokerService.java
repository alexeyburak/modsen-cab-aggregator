package com.modsen.cabaggregator.rideservice.service;

import com.modsen.cabaggregator.rideservice.dto.NotificationDto;

public interface NotificationBrokerService {
    void send(String topic, NotificationDto dto);
}
