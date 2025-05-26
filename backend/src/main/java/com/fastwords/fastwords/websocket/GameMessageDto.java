package com.fastwords.fastwords.websocket;

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
public class GameMessageDto {

    private String type;    
    private String content; 
    private String sender; 
    private Long gameId;
}
