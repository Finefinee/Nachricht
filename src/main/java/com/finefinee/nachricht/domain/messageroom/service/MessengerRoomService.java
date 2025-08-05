package com.finefinee.nachricht.domain.messageroom.service;

import com.finefinee.nachricht.domain.messageroom.dto.CreateMessengerRoomRequest;
import com.finefinee.nachricht.domain.messageroom.entity.MessengerRoom;
import com.finefinee.nachricht.domain.messageroom.repository.MessengerRoomRepository;
import com.finefinee.nachricht.domain.user.entity.UserEntity;
import com.finefinee.nachricht.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessengerRoomService {

    private final UserRepository userRepository;
    private final MessengerRoomRepository messengerRoomRepository;

    public MessengerRoom createOrGetRoom(CreateMessengerRoomRequest request) {
        List<String> usernames = request.participantUsernames();

        if (usernames.size() != 2) {
            throw new IllegalArgumentException("참가자는 정확히 2명이어야 합니다.");
        }

        List<UserEntity> participants = usernames.stream()
                .map(username -> userRepository.findById(username)
                        .orElseThrow(() -> new RuntimeException("유저 없음: " + username)))
                .toList();

        UserEntity userA = participants.get(0);
        UserEntity userB = participants.get(1);

        return messengerRoomRepository.findByParticipants(userA, userB)
                .orElseGet(() -> {
                    MessengerRoom room = MessengerRoom.builder()
                            .participants(participants)
                            .build();
                    return messengerRoomRepository.save(room);
                });
    }

    public String findReceiverUsername(Long roomId, String senderUsername) {
        MessengerRoom room = messengerRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("방 없음"));

        // 방 참여자 리스트 중에서 sender가 아닌 사람 찾기 (1대1 가정)
        return room.getParticipants().stream()
                .map(UserEntity::getUsername)
                .filter(username -> !username.equals(senderUsername))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("받는 사람 없음"));
    }

}
