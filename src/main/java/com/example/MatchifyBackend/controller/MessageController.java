package com.example.MatchifyBackend.controller;

import com.example.MatchifyBackend.dto.MessageRequest;
import com.example.MatchifyBackend.dto.MessageResponse;
import com.example.MatchifyBackend.service.AuthHelperService;
import com.example.MatchifyBackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private AuthHelperService authHelperService;

    @PostMapping
    public MessageResponse sendMessage(@RequestBody MessageRequest request) {
        Long currentUserId = authHelperService.getCurrentUser().getId();
        return messageService.sendMessage(request, currentUserId);
    }

    @GetMapping("/{otherUserId}")
    public List<MessageResponse> getMessages(@PathVariable Long otherUserId) {
        Long currentUserId = authHelperService.getCurrentUser().getId();
        return messageService.getMessages(currentUserId, otherUserId);
    }
}