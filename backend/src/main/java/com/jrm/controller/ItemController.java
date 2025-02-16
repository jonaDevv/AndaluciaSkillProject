package com.jrm.controller;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jrm.dto.converter.ConverterDto;
import com.jrm.dto.item.ItemResponseDto;
import com.jrm.service.ItemService;
import com.jrm.service.TestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    
    private final ItemService itemService;
    private final TestService testService;
    private final ConverterDto genericDto;
    

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


}
