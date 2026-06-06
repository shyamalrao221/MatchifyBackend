package com.example.MatchifyBackend.controller;

import com.example.MatchifyBackend.dto.MatchResponseDto;
import com.example.MatchifyBackend.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/{userId}")
    public List<MatchResponseDto> getMatches(@PathVariable Long userId) {
        return matchService.getMatches(userId);
    }
}