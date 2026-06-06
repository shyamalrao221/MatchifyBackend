package com.example.MatchifyBackend.repository;

import com.example.MatchifyBackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderBySentAtAsc(
            Long sender1, Long receiver1,
            Long sender2, Long receiver2
    );
}