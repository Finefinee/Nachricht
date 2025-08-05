package com.finefinee.nachricht.domain.message.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record SendMessageRequest(
        String senderUsername,
        Long roomId,
        String content,
        LocalDateTime when
) {
}
