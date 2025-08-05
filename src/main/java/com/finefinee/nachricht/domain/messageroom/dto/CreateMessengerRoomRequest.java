package com.finefinee.nachricht.domain.messageroom.dto;

import java.util.List;

public record CreateMessengerRoomRequest(
        List<String> participantUsernames
) {
}
