package com.finefinee.nachricht.domain.messageroom.repository;

import com.finefinee.nachricht.domain.messageroom.entity.MessengerRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessengerRoomRepository extends JpaRepository<MessengerRoom, Long> {

    @Query("SELECT mr FROM MessengerRoom mr JOIN mr.participants p WHERE p.username = :username")
    List<MessengerRoom> findByParticipantsUsername(@Param("username") String username);

    @Query("SELECT mr FROM MessengerRoom mr WHERE SIZE(mr.participants) = 2 AND " +
           "EXISTS (SELECT p1 FROM mr.participants p1 WHERE p1.username = :user1) AND " +
           "EXISTS (SELECT p2 FROM mr.participants p2 WHERE p2.username = :user2)")
    Optional<MessengerRoom> findByTwoParticipants(@Param("user1") String user1, @Param("user2") String user2);
}
