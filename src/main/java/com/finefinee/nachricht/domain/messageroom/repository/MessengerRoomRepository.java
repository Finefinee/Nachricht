package com.finefinee.nachricht.domain.messageroom.repository;

import com.finefinee.nachricht.domain.messageroom.entity.MessengerRoom;
import com.finefinee.nachricht.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MessengerRoomRepository extends JpaRepository<MessengerRoom, Long> {

    @Query("SELECT r FROM MessengerRoom r JOIN r.participants p1 JOIN r.participants p2 " +
            "WHERE p1 = :userA AND p2 = :userB")
    Optional<MessengerRoom> findByParticipants(@Param("userA") UserEntity userA, @Param("userB") UserEntity userB);
}
