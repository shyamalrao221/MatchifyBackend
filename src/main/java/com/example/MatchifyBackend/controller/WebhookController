package com.example.MatchifyBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebhookController {

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody(required = false) String payload,
            @RequestHeader(value = "X-GitHub-Event", required = false) String event
    ) {
        System.out.println("GitHub event: " + event);
        System.out.println("Payload: " + payload);
        return ResponseEntity.ok("Webhook received");
    }
}
