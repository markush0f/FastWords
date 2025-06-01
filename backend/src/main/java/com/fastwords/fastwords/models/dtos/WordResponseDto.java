package com.fastwords.fastwords.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordResponseDto {
    
    public Long id;
    public String word;
    public Long collectionId;
}
