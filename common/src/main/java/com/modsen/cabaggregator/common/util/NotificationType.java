package com.modsen.cabaggregator.common.util;

import lombok.Getter;

@Getter
public enum NotificationType {

    START_RIDE("start-ride-topic"),
    REJECT_RIDE("reject-ride-topic"),
    FINISH_RIDE("finish-ride-topic"),
    CHANGE_PAYMENT_STATUS("change-payment-status-topic");

    private final String brokerTopic;

    NotificationType(String brokerTopic) {
        this.brokerTopic = brokerTopic;
    }

}
