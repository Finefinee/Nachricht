package com.finefinee.nachricht.domain.message.repository;

import com.finefinee.nachricht.domain.message.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByRoomIdOrderBySentAtAsc(Long roomId);
}
