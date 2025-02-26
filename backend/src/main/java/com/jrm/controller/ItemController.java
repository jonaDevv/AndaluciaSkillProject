// package com.jrm.controller;

// import java.util.List;
// import java.util.Optional;

// import org.apache.catalina.connector.Response;
// import com.jrm.model.Item;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.jrm.dto.converter.ConverterDto;
// import com.jrm.dto.item.ItemCreateDto;
// import com.jrm.dto.item.ItemResponseDto;
// import com.jrm.dto.item.ItemUpdateDto;
// import com.jrm.error.item.ItemNotFoundException;
// import com.jrm.error.prueba.PruebaNotFoundException;
// import com.jrm.model.Prueba;
// import com.jrm.model.Specialty;
// import com.jrm.model.TestSimpleDto;
// import com.jrm.service.ItemService;
// import com.jrm.service.PruebaService;
// import com.jrm.service.SpecialtyService;


// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/item")
// @RequiredArgsConstructor
// public class ItemController {
    
//     private final ItemService itemService;
//     private final PruebaService pruebaService;
//     private final ConverterDto genericDto;
//     private final SpecialtyService specialtyService;
    

//     @GetMapping
//     public ResponseEntity<List<ItemResponseDto>> getAllItems() {
        
//         List<ItemResponseDto> items = itemService.findAll().stream()
//                 .map(i -> genericDto.genericConvert(i, ItemResponseDto.class))
//                 .toList();
        
//         return Optional.of(items)
//                 .filter(list -> !list.isEmpty())
//                 .map(nonEmptyList -> nonEmptyList.stream()
//                     .map(i -> genericDto.genericConvert(i, ItemResponseDto.class))
//                     .toList())
//                 .map(ResponseEntity::ok)
//                 .orElseGet(() -> ResponseEntity.notFound().build());

//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Long id) {
//         return Optional.ofNullable(itemService.findById(id))
//                 .map(i -> genericDto.genericConvert(i, ItemResponseDto.class))
//                 .map(ResponseEntity::ok)
//                 .orElseThrow(() -> new ItemNotFoundException(id));
//     }

//     @PostMapping
//     public ResponseEntity<?> createItem( @RequestBody ItemCreateDto i) {
        
//         System.out.println("Creando item"+i);

//         Item item = genericDto.genericConvert(i, Item.class);

//         Prueba prueba = pruebaService.findById(i.getPrueba())
//                 .orElseThrow(() -> new PruebaNotFoundException(i.getPrueba()));
        
//         item.setPrueba(prueba);
//         //  Guardar y retornar respuesta
//         Item savedItem = itemService.save(item);

//         ItemResponseDto responseDto = genericDto.genericConvert(savedItem, ItemResponseDto.class);

//         return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//     }

//     @PostMapping("/test-simple")
//     public ResponseEntity<?> testSimple(@RequestBody TestSimpleDto dto) {

//         System.out.println("Cuerpo recibido: " + dto);
//         System.out.println("Mensaje recibido: " + dto.getMensaje());
       
//         return ResponseEntity.ok("recibido: " + dto.getMensaje());
//     }

//     // Actualizar un item
//     @PutMapping("/{id}")
//     public ResponseEntity<?> updateItem( @PathVariable Long id,
//             @Valid @RequestBody ItemUpdateDto itemUpdateDTO) {
        
//         return itemService.findById(id)
//                 .map(item -> {
//                     // Actualizar campos básicos
//                     item.setDescription(itemUpdateDTO.getDescription());
//                     item.setWeight(itemUpdateDTO.getWeight());
//                     item.setPercentage(itemUpdateDTO.getPercentage());
                    
//                     // Manejar specialty
//                     Optional.ofNullable(itemUpdateDTO.getPrueba())
//                             .ifPresent(pruebaId -> {
//                                 item.setPrueba(pruebaService.findById(pruebaId)
//                                 .orElseThrow(() -> new PruebaNotFoundException(pruebaId)));
//                             });
                    
//                     // Guardar cambios y retornar el usuario actualizado
//                     return itemService.update(id, item); // ¡Aquí falta el return!
//                 })
//                 .map(p->genericDto.genericConvert(p, ItemResponseDto.class))
//                 .map(ResponseEntity::ok)
//                 .orElseThrow(() -> new ItemNotFoundException(id));
        
//     }


//     // Eliminar un item
//     @DeleteMapping("/{id}")
//     public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        
//         return itemService.findById(id)
//                 .map(p -> {
//                     itemService.delete(id); // Eliminar al usuario
//                     return ResponseEntity.ok().build(); // Retornar una respuesta vacía con estado 200 OK
//                 })
//                 .orElseThrow(() -> new ItemNotFoundException(id)); // Lanzar excepción si no se encuentra el usuario
//     }


// }

package com.jrm.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jrm.dto.converter.ConverterDto;
import com.jrm.dto.item.ItemCreateDto;
import com.jrm.dto.item.ItemResponseDto;
import com.jrm.dto.item.ItemUpdateDto;
import com.jrm.error.ApiError;
import com.jrm.error.item.ItemNotFoundException;
import com.jrm.error.prueba.PruebaNotFoundException;
import com.jrm.model.Item;
import com.jrm.model.Prueba;
import com.jrm.service.ItemService;
import com.jrm.service.PruebaService;

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
@RequestMapping("/item")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Gestión de Items", description = "Operaciones CRUD para la gestión de items de evaluación")
public class ItemController {
    
    private final ItemService itemService;
    private final PruebaService pruebaService;
    private final ConverterDto genericDto;

    // Obtener todos los Items
    @Operation(summary = "Obtener todos los items", 
               description = "Retorna una lista de todos los items de evaluación registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de items encontrada",
                   content = @Content(schema = @Schema(implementation = ItemResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron items",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItems() {
        List<ItemResponseDto> items = itemService.findAll().stream()
                .map(i -> genericDto.genericConvert(i, ItemResponseDto.class))
                .toList();
        
        return items.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(items);
    }

    // Obtener un Item por su ID
    @Operation(summary = "Obtener item por ID", 
    description = "Busca un item específico por su identificador único")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Item encontrado",
            content = @Content(schema = @Schema(implementation = ItemResponseDto.class))),
    @ApiResponse(responseCode = "404", description = "Item no encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Long id) {
        return Optional.ofNullable(itemService.findById(id))
                .map(i -> genericDto.genericConvert(i, ItemResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }


    // Crear un nuevo Item
    @Operation(summary = "Crear nuevo item", 
               description = "Crea un nuevo item de evaluación con los datos proporcionados")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Item creado exitosamente",
                   content = @Content(schema = @Schema(implementation = ItemResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Prueba relacionada no encontrada",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@RequestBody @Valid ItemCreateDto itemCreateDto) {
        // Convertir DTO a entidad
        Item item = genericDto.genericConvert(itemCreateDto, Item.class);

        // Obtener la prueba relacionada
        Prueba prueba = pruebaService.findById(itemCreateDto.getPrueba())
                .orElseThrow(() -> new PruebaNotFoundException(itemCreateDto.getPrueba()));

        // Establecer la relación con la prueba
        item.setPrueba(prueba);

        // Guardar el Item y convertir a DTO de respuesta
        Item savedItem = itemService.save(item);

        ItemResponseDto responseDto = genericDto.genericConvert(savedItem, ItemResponseDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // Actualizar un Item
    @Operation(summary = "Actualizar item existente", 
    description = "Actualiza los datos de un item de evaluación existente")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Item actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = ItemResponseDto.class))),
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
    @ApiResponse(responseCode = "404", description = "Item o prueba relacionada no encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> updateItem(@PathVariable Long id,
                                                      @Valid @RequestBody ItemUpdateDto itemUpdateDto) {
        return itemService.findById(id)
                .map(item -> {
                    // Actualizar campos básicos del Item
                    item.setDescription(itemUpdateDto.getDescription());
                    item.setWeight(itemUpdateDto.getWeight());
                    item.setPercentage(itemUpdateDto.getPercentage());

                    // Actualizar la prueba si es necesario
                    if (itemUpdateDto.getPrueba() != null) {
                        Prueba prueba = pruebaService.findById(itemUpdateDto.getPrueba())
                                .orElseThrow(() -> new PruebaNotFoundException(itemUpdateDto.getPrueba()));
                        item.setPrueba(prueba);
                    }

                    // Guardar los cambios
                    Item updatedItem = itemService.update(id, item);
                    return genericDto.genericConvert(updatedItem, ItemResponseDto.class);
                })
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    // Eliminar un Item
    @Operation(summary = "Eliminar item", 
               description = "Elimina permanentemente un item de evaluación del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        return itemService.findById(id)
                .map(item -> {
                    itemService.delete(id); // Eliminar el item
                    return ResponseEntity.ok().build(); // Responder con OK
                })
                .orElseThrow(() -> new ItemNotFoundException(id)); // Lanzar excepción si no se encuentra
    }
}




