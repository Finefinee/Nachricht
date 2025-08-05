package com.finefinee.nachricht.domain.message.service;

import com.finefinee.nachricht.domain.message.dto.SendMessageRequest;
import com.finefinee.nachricht.domain.message.entity.MessageEntity;
import com.finefinee.nachricht.domain.message.repository.MessageRepository;
import com.finefinee.nachricht.domain.messageroom.entity.MessengerRoom;
import com.finefinee.nachricht.domain.messageroom.repository.MessengerRoomRepository;
import com.finefinee.nachricht.domain.user.entity.UserEntity;
import com.finefinee.nachricht.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessengerRoomRepository roomRepository;

    public void sendMessage(SendMessageRequest request) {
        UserEntity sender = userRepository.findByUsername(request.senderUsername())
                .orElseThrow(() -> new RuntimeException("보낸 사람 없음"));

        MessengerRoom room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RuntimeException("채팅방 없음"));

        MessageEntity message = MessageEntity.builder()
                .sender(sender)
                .room(room)
                .content(request.content())
                .sentAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);
        System.out.println("저장됨");
    }

    public List<MessageEntity> getMessages(Long roomId) {
        return messageRepository.findByRoomIdOrderBySentAtAsc(roomId);
    }

}