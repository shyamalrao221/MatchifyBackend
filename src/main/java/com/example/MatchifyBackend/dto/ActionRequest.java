package com.example.MatchifyBackend.dto;

import com.example.MatchifyBackend.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActionRequest {
    private ActionType actionType;
    private Long targetUserId;
}
