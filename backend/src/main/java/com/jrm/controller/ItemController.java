package com.jrm.controller;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import com.jrm.model.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jrm.dto.converter.ConverterDto;
import com.jrm.dto.item.ItemCreateDto;
import com.jrm.dto.item.ItemResponseDto;
import com.jrm.dto.item.ItemUpdateDto;
import com.jrm.error.item.ItemNotFoundException;
import com.jrm.error.prueba.PruebaNotFoundException;
import com.jrm.model.Prueba;
import com.jrm.model.Specialty;
import com.jrm.model.TestSimpleDto;
import com.jrm.service.ItemService;
import com.jrm.service.PruebaService;
import com.jrm.service.SpecialtyService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    
    private final ItemService itemService;
    private final PruebaService pruebaService;
    private final ConverterDto genericDto;
    private final SpecialtyService specialtyService;
    

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItems() {
        
        List<ItemResponseDto> items = itemService.findAll().stream()
                .map(i -> genericDto.genericConvert(i, ItemResponseDto.class))
                .toList();
        
        return Optional.of(items)
                .filter(list -> !list.isEmpty())
                .map(nonEmptyList -> nonEmptyList.stream()
                    .map(i -> genericDto.genericConvert(i, ItemResponseDto.class))
                    .toList())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Long id) {
        return Optional.ofNullable(itemService.findById(id))
                .map(i -> genericDto.genericConvert(i, ItemResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<?> createItem( @RequestBody ItemCreateDto i) {
        
        System.out.println("Creando item"+i);

        Item item = genericDto.genericConvert(i, Item.class);

        Prueba prueba = pruebaService.findById(i.getPrueba())
                .orElseThrow(() -> new PruebaNotFoundException(i.getPrueba()));
        
        item.setPrueba(prueba);
        //  Guardar y retornar respuesta
        Item savedItem = itemService.save(item);

        ItemResponseDto responseDto = genericDto.genericConvert(savedItem, ItemResponseDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/test-simple")
    public ResponseEntity<?> testSimple(@RequestBody TestSimpleDto dto) {

        System.out.println("Cuerpo recibido: " + dto);
        System.out.println("Mensaje recibido: " + dto.getMensaje());
       
        return ResponseEntity.ok("recibido: " + dto.getMensaje());
    }

    // Actualizar un item
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem( @PathVariable Long id,
            @Valid @RequestBody ItemUpdateDto itemUpdateDTO) {
        
        return itemService.findById(id)
                .map(item -> {
                    // Actualizar campos básicos
                    item.setDescription(itemUpdateDTO.getDescription());
                    item.setWeight(itemUpdateDTO.getWeight());
                    item.setPercentage(itemUpdateDTO.getPercentage());
                    
                    // Manejar specialty
                    Optional.ofNullable(itemUpdateDTO.getPrueba())
                            .ifPresent(pruebaId -> {
                                item.setPrueba(pruebaService.findById(pruebaId)
                                .orElseThrow(() -> new PruebaNotFoundException(pruebaId)));
                            });
                    
                    // Guardar cambios y retornar el usuario actualizado
                    return itemService.update(id, item); // ¡Aquí falta el return!
                })
                .map(p->genericDto.genericConvert(p, ItemResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ItemNotFoundException(id));
        
    }


    // Eliminar un item
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        
        return itemService.findById(id)
                .map(p -> {
                    itemService.delete(id); // Eliminar al usuario
                    return ResponseEntity.ok().build(); // Retornar una respuesta vacía con estado 200 OK
                })
                .orElseThrow(() -> new ItemNotFoundException(id)); // Lanzar excepción si no se encuentra el usuario
    }


}
