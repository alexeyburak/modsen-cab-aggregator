package com.modsen.cabaggregator.rideservice.config;

import com.modsen.cabaggregator.common.util.NotificationType;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic startRideTopic() {
        return TopicBuilder.name(NotificationType.START_RIDE.getBrokerTopic())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic rejectRideTopic() {
        return TopicBuilder.name(NotificationType.REJECT_RIDE.getBrokerTopic())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic finishRideTopic() {
        return TopicBuilder.name(NotificationType.FINISH_RIDE.getBrokerTopic())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic changePaymentStatusTopic() {
        return TopicBuilder.name(NotificationType.CHANGE_PAYMENT_STATUS.getBrokerTopic())
                .partitions(1)
                .replicas(1)
                .build();
    }

}
