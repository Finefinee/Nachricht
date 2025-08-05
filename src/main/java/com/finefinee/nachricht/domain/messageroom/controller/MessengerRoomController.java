package com.finefinee.nachricht.domain.messageroom.controller;

import com.finefinee.nachricht.domain.messageroom.dto.CreateMessengerRoomRequest;
import com.finefinee.nachricht.domain.messageroom.entity.MessengerRoom;
import com.finefinee.nachricht.domain.messageroom.service.MessengerRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class MessengerRoomController {

    private final MessengerRoomService messengerRoomService;

    /**
     * 두 사용자 간의 채팅방을 생성하거나 기존 방을 반환합니다
     */
    @PostMapping("/create")
    public ResponseEntity<MessengerRoom> createOrFindRoom(
            @RequestBody CreateMessengerRoomRequest createMessengerRoomRequest) {
        MessengerRoom room = messengerRoomService.findOrCreateRoom(createMessengerRoomRequest.participants().get(0), createMessengerRoomRequest.participants().get(1));
        return ResponseEntity.ok(room);
    }

    /**
     * 특정 사용자가 참여한 모든 채팅방을 조회합니다
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<List<MessengerRoom>> getUserRooms(@PathVariable String username) {
        List<MessengerRoom> rooms = messengerRoomService.getUserRooms(username);
        return ResponseEntity.ok(rooms);
    }
}
