package com.jrm.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jrm.dto.converter.ConverterDto;
import com.jrm.dto.evaluacion.EvaluacionDetailsDTO;
import com.jrm.dto.evaluacion.EvaluacionResponseDto;
import com.jrm.dto.evaluacionItem.EvaluacionItemCreateDto;
import com.jrm.dto.evaluacionItem.EvaluacionItemDTO;
import com.jrm.dto.evaluacionItem.EvaluacionItemResponseDto;
import com.jrm.dto.evaluacionItem.EvaluacionItemUpdateDto;
import com.jrm.error.ApiError;
import com.jrm.error.evaluacion.EvaluacionNotFoundException;
import com.jrm.error.evaluacionitem.EvaluacionItemNotFoundException;
import com.jrm.model.Evaluacion;
import com.jrm.model.EvaluacionItem;
import com.jrm.model.Item;
import com.jrm.service.EvaluacionItemService;
import com.jrm.service.EvaluacionService;
import com.jrm.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/evaluacionitem")
@RequiredArgsConstructor
@Tag(name = "Items de Evaluación", description = "Gestión de items individuales de evaluación")
@SecurityRequirement(name = "bearerAuth")
public class EvaluacionItemController {

    private final EvaluacionService evService;
    private final ConverterDto genericConverter;
    private final ItemService itemService;
    private final EvaluacionItemService evItemService;


    @Operation(summary = "Obtener todos los items de evaluación", 
               description = "Lista completa de items de evaluación registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de items obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron items",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllEvaluacionItems() {
        List<EvaluacionItem> evaluacionItems = evItemService.findAll();
        return Optional.of(evaluacionItems)
                .filter(list -> !list.isEmpty())
                .map(nomEmtyList -> nomEmtyList.stream()
                        .map(e -> genericConverter.genericConvert(e, EvaluacionItemResponseDto.class))
                        .toList())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @Operation(summary = "Obtener item por ID", 
               description = "Obtener un item de evaluación específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item encontrado",
                   content = @Content(schema = @Schema(implementation = EvaluacionItemResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Item no encontrado",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/id")
    public ResponseEntity<?> getEvaluacionItemId(@PathVariable Long id)
    {
         return Optional.ofNullable(evItemService.findById(id))
                .map(e -> genericConverter.genericConvert(e, EvaluacionItemResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EvaluacionItemNotFoundException(id));

    }


    @Operation(summary = "Crear nuevo item de evaluación", 
               description = "Crear un nuevo item de evaluación con los datos proporcionados")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Item creado exitosamente",
                   content = @Content(schema = @Schema(implementation = EvaluacionItemResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<?> createEvaluacionItem(@RequestBody EvaluacionItemCreateDto dto) {
        
        EvaluacionItem evaluacionItem = genericConverter.genericConvert(dto, EvaluacionItem.class);

        evaluacionItem.setEvaluacion(genericConverter.genericConvert(dto.getEvaluacionId(), Evaluacion.class));
        evaluacionItem.setItem(genericConverter.genericConvert(dto.getItem(), Item.class));

        EvaluacionItem savedEvaluacionItem = evItemService.save(evaluacionItem);

        EvaluacionItemResponseDto responseDto = genericConverter.genericConvert(savedEvaluacionItem, EvaluacionItemResponseDto.class);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @Operation(summary = "Actualizar item de evaluación", 
    description = "Actualizar un item de evaluación existente")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Item actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = EvaluacionItemResponseDto.class))),
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
    @ApiResponse(responseCode = "404", description = "Item no encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvaluacionItem(@PathVariable Long id, @Valid @RequestBody EvaluacionItemUpdateDto dto) {
        
        return evItemService.findById(id)
                .map(evaluacionItem -> {
                    
                    evaluacionItem.setValoracion(dto.getValoracion());
                    evaluacionItem.setJustificacion(dto.getJustificacion());
                    return evItemService.update(id, evaluacionItem); // ¡Aquí falta el return!
                })
                .map(p->genericConverter.genericConvert(p, EvaluacionItemResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EvaluacionItemNotFoundException(id));
        
    }


    @Operation(summary = "Eliminar item de evaluación", 
               description = "Eliminar permanentemente un item de evaluación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvaluacionItem(@PathVariable Long id) {
        
        return evItemService.findById(id)
                .map(p -> {
                    evItemService.delete(id); // Eliminar al usuario
                    return ResponseEntity.ok().build(); // Retornar una respuesta vacía con estado 200 OK
                })
                .orElseThrow(() -> new EvaluacionItemNotFoundException(id)); // Lanzar excepción si no se encuentra el usuario
    }



























    public EvaluacionItem updateEvaluacionItem(Long itemId, EvaluacionItemDTO dto) {
        EvaluacionItem item = evItemService.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Ítem de evaluación no encontrado"));
        
        item.setValoracion(dto.getValoracion());
        item.setJustificacion(dto.getJustificacion());
        
        return evItemService.save(item);
    }

    public EvaluacionDetailsDTO getEvaluacionDetails(Long id) {
    Evaluacion evaluacion = evService.findByIdWithItems(id);
    return evService.mapToDetailsDTO(evaluacion);
    }
    



}
