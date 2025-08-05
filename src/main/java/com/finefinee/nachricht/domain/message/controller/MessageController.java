package com.finefinee.nachricht.domain.message.controller;

import com.finefinee.nachricht.domain.message.dto.SendMessageRequest;
import com.finefinee.nachricht.domain.message.entity.MessageEntity;
import com.finefinee.nachricht.domain.message.service.MessageService;
import com.finefinee.nachricht.domain.messageroom.service.MessengerRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final MessengerRoomService messengerRoomService;

    // /app/chat.send 로 메시지 들어오면 실행됨
    @MessageMapping("/chat.send")
    public void sendMessage(SendMessageRequest request) {
        // 메시지 DB에 저장
        MessageEntity savedMessage = messageService.sendMessage(request);

        String receiver = messengerRoomService.findReceiverUsername(request.roomId(), request.senderUsername());
        String destination = "/queue/user." + receiver;

        // 저장된 메시지를 DTO로 변환해서 보내도 좋음 (간단히 바로 보내도 됨)
        messagingTemplate.convertAndSend(destination, request);
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(@RequestParam Long roomId) {
        return ResponseEntity.ok().body(messageService.getMessages(roomId));
    }
}
