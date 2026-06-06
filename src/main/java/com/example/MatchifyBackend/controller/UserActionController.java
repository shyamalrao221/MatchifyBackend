package com.example.MatchifyBackend.controller;

import com.example.MatchifyBackend.dto.ActionRequest;
import com.example.MatchifyBackend.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actions")
public class UserActionController {

    @Autowired
    private UserActionService userActionService;

    @PostMapping
    public void performAction(@RequestBody ActionRequest request, @RequestParam Long userId) {
        // Call the service to like a user
         userActionService.performAction(request,userId);
    }
}
