package com.fastwords.fastwords.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchmakingRequest {
    private String playerId;   
    private Long collectionId; 
}

