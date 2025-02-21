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
import com.jrm.dto.evaluacion.EvaluacionResponseDto;
import com.jrm.dto.evaluacionItem.EvaluacionItemCreateDto;
import com.jrm.dto.evaluacionItem.EvaluacionItemResponseDto;
import com.jrm.dto.evaluacionItem.EvaluacionItemUpdateDto;
import com.jrm.error.evaluacion.EvaluacionNotFoundException;
import com.jrm.error.evaluacionitem.EvaluacionItemNotFoundException;
import com.jrm.model.Evaluacion;
import com.jrm.model.EvaluacionItem;
import com.jrm.model.Item;
import com.jrm.service.EvaluacionItemService;
import com.jrm.service.EvaluacionService;
import com.jrm.service.ItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/evaluacionitem")
@RequiredArgsConstructor
public class EvaluacionItemController {

    private final EvaluacionService evService;
    private final ConverterDto genericConverter;
    private final ItemService itemService;
    private final EvaluacionItemService evItemService;



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


    @GetMapping("/id")
    public ResponseEntity<?> getEvaluacionItemId(@PathVariable Long id)
    {
         return Optional.ofNullable(evItemService.findById(id))
                .map(e -> genericConverter.genericConvert(e, EvaluacionItemResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EvaluacionItemNotFoundException(id));

    }


    @PostMapping
    public ResponseEntity<?> createEvaluacionItem(@RequestBody EvaluacionItemCreateDto dto) {
        
        EvaluacionItem evaluacionItem = genericConverter.genericConvert(dto, EvaluacionItem.class);

        evaluacionItem.setEvaluacion(genericConverter.genericConvert(dto.getEvaluacionId(), Evaluacion.class));
        evaluacionItem.setItem(genericConverter.genericConvert(dto.getItem(), Item.class));

        EvaluacionItem savedEvaluacionItem = evItemService.save(evaluacionItem);

        EvaluacionItemResponseDto responseDto = genericConverter.genericConvert(savedEvaluacionItem, EvaluacionItemResponseDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvaluacionItem(@PathVariable Long id) {
        
        return evItemService.findById(id)
                .map(p -> {
                    evItemService.delete(id); // Eliminar al usuario
                    return ResponseEntity.ok().build(); // Retornar una respuesta vacía con estado 200 OK
                })
                .orElseThrow(() -> new EvaluacionItemNotFoundException(id)); // Lanzar excepción si no se encuentra el usuario
    }

    



}
