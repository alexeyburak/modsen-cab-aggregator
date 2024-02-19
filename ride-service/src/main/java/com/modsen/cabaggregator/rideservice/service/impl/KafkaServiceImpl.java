package com.modsen.cabaggregator.rideservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.cabaggregator.rideservice.dto.NotificationDto;
import com.modsen.cabaggregator.rideservice.service.NotificationBrokerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaServiceImpl implements NotificationBrokerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void send(String topic, NotificationDto dto) {
        try {
            kafkaTemplate.send(
                    topic,
                    objectMapper.writeValueAsString(dto)
            );
        } catch (JsonProcessingException e) {
            log.error("Error while send to kafka topic {}", topic);
        }
    }

}
