package com.fastwords.fastwords.models.dtos;

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
public class GameResponseDto {

    private String name;

    private Long player1Id;

    private Long player2Id;

    private Long collectionId;

    private String status;
}
