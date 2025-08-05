package com.finefinee.nachricht.domain.messageroom.dto;

import java.util.ArrayList;
import java.util.List;

public record CreateMessengerRoomRequest(
        List<String> participants
) {
}
