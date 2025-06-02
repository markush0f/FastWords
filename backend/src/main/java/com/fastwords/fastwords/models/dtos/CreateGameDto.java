package com.fastwords.fastwords.models.dtos;

import com.fastwords.fastwords.common.enums.GameStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor 
@NoArgsConstructor
public class CreateGameDto {

    private String name;

    private Long player1Id;

    private Long player2Id;

    private Long collectionId;

    private GameStatus gameStatus;

}
