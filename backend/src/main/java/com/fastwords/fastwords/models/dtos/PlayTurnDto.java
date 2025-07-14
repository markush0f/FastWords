package com.fastwords.fastwords.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayTurnDto {
    private String gameId;
    private String playerId;
    private String word;
}