package com.jrm.dto.item;


import org.springframework.validation.annotation.Validated;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class ItemResponseDto {

    private Long id;

   
    private String description;

    
    
    private int weight;

   
    private float percentage;

 
    private String pruebaName;

}
