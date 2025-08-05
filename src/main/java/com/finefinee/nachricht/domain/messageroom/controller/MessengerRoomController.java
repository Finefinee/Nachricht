package com.finefinee.nachricht.domain.messageroom.controller;

import com.finefinee.nachricht.domain.messageroom.dto.CreateMessengerRoomRequest;
import com.finefinee.nachricht.domain.messageroom.entity.MessengerRoom;
import com.finefinee.nachricht.domain.messageroom.service.MessengerRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class MessengerRoomController {

    private final MessengerRoomService messengerRoomService;

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody CreateMessengerRoomRequest request) {
        MessengerRoom room = messengerRoomService.createOrGetRoom(request);
        return ResponseEntity.ok(room);
    }
}
