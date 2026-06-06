package com.example.MatchifyBackend.service;

import com.example.MatchifyBackend.dto.MessageRequest;
import com.example.MatchifyBackend.dto.MessageResponse;
import com.example.MatchifyBackend.entity.Message;
import com.example.MatchifyBackend.entity.User;
import com.example.MatchifyBackend.exception.ResourceNotFoundException;
import com.example.MatchifyBackend.repository.MessageRepository;
import com.example.MatchifyBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public MessageResponse sendMessage(MessageRequest request, Long senderId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender with id " + senderId + " not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver with id " + request.getReceiverId() + " not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());
        message.setSentAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);

        return mapToResponse(savedMessage);
    }

    public List<MessageResponse> getMessages(Long userId, Long otherUserId) {
        List<Message> messages =
                messageRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderBySentAtAsc(
                        userId, otherUserId,
                        otherUserId, userId
                );

        return messages.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private MessageResponse mapToResponse(Message message) {
        String senderName = (message.getSender().getFirstName() + " " + message.getSender().getLastName()).trim();
        String receiverName = (message.getReceiver().getFirstName() + " " + message.getReceiver().getLastName()).trim();

        return new MessageResponse(
                message.getId(),
                message.getSender().getId(),
                senderName,
                message.getReceiver().getId(),
                receiverName,
                message.getContent(),
                message.getSentAt()
        );
    }
}