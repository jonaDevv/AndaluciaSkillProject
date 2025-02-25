package com.jrm.dto.prueba;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import com.jrm.dto.item.ItemResponseDto;
import com.jrm.model.Specialty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data  @AllArgsConstructor
@NoArgsConstructor
@Validated
public class PruebaResponseDto {

    @Schema(description = "ID de la prueba", example = "1")
    private Long id;
    
    @Schema(description = "Enunciado de la prueba", example = "Resolución de matrices")
    private String enunciado;

    @Schema(description = "Puntuacion máxima de la prueba", example = "100")
    private float maxScore;

    @Schema(description = "URL del PDF asociado", example = "http://ejemplo.com/instrucciones.pdf")
    private String pdfUrl;
    
    @Schema(description = "Nombre de la especialidad", example = "Matemáticas")
    private String specialtyName;

    @Schema(description = "Lista de item", example = "Matriz, Vector, Determinante")
    private List<ItemResponseDto> items = new ArrayList<>();

}
