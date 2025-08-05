package com.finefinee.nachricht.domain.messageroom.service;

import com.finefinee.nachricht.domain.messageroom.entity.MessengerRoom;
import com.finefinee.nachricht.domain.messageroom.repository.MessengerRoomRepository;
import com.finefinee.nachricht.domain.user.entity.UserEntity;
import com.finefinee.nachricht.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessengerRoomService {

    private final MessengerRoomRepository messengerRoomRepository;
    private final UserRepository userRepository;

    /**
     * 채팅방에서 발신자가 아닌 다른 참가자(수신자)의 사용자명을 찾습니다
     */
    public String findReceiverUsername(Long roomId, String senderUsername) {
        MessengerRoom room = messengerRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다"));

        return room.getParticipants().stream()
                .filter(user -> !user.getUsername().equals(senderUsername))
                .findFirst()
                .map(UserEntity::getUsername)
                .orElseThrow(() -> new RuntimeException("수신자를 찾을 수 없습니다"));
    }

    /**
     * 두 사용자 간의 채팅방을 찾거나 생성합니다
     */
    public MessengerRoom findOrCreateRoom(String user1, String user2) {
        Optional<MessengerRoom> existingRoom = messengerRoomRepository.findByTwoParticipants(user1, user2);

        if (existingRoom.isPresent()) {
            return existingRoom.get();
        }

        // 새 채팅방 생성
        UserEntity userEntity1 = userRepository.findByUsername(user1)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + user1));
        UserEntity userEntity2 = userRepository.findByUsername(user2)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + user2));

        MessengerRoom newRoom = MessengerRoom.builder()
                .participants(List.of(userEntity1, userEntity2))
                .build();

        return messengerRoomRepository.save(newRoom);
    }

    /**
     * 사용자가 참여한 모든 채팅방을 조회합니다
     */
    public List<MessengerRoom> getUserRooms(String username) {
        return messengerRoomRepository.findByParticipantsUsername(username);
    }
}

