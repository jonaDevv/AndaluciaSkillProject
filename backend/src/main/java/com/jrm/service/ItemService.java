package com.jrm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrm.dto.item.ItemCreateDto;
import com.jrm.error.item.ItemNotFoundException;
import com.jrm.model.Item;
import com.jrm.repository.ItemRepository;
import com.jrm.service.base.BaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService implements BaseService<Item, Long> {

    private final ItemRepository itemRepository;

    @Override
    public List<Item> findAll() {
        
        return itemRepository.findAll();

    }

    @Override
    public Optional<Item> findById(Long id) {
        
        return itemRepository.findById(id);
    }

    @Override
    public Item save(Item item) {
        
        return itemRepository.save(item);
    }

    public List<Item> saveAll(List<Item> items) {
        
        return itemRepository.saveAll(items);
    }

    @Override
    public Item update(Long id, Item t) {
        
        return itemRepository.save(t);
    }

    @Override
    public void delete(Long id) {
        
        Item item = itemRepository.findById(id)
                                    .orElseThrow(() -> new ItemNotFoundException(id));
        itemRepository.delete(item);
    }

     public List<ItemCreateDto> parseItemsJson(String itemsJson) {
        try {
            return new ObjectMapper().readValue(itemsJson, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear items: " + e.getMessage());
        }
    }

    

}
