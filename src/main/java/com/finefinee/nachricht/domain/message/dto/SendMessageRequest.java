package com.finefinee.nachricht.domain.message.dto;

import java.time.LocalTime;

public record SendMessageRequest(
        String senderUsername,
        Long roomId,
        String content,
        LocalTime when
) {
}
