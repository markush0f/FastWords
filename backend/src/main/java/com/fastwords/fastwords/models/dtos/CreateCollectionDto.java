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
public class CreateCollectionDto {
    private String name;
}
