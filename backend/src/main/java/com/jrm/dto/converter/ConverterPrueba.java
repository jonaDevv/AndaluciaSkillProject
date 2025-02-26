package com.jrm.dto.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import com.jrm.dto.item.ItemResponseDto;
import com.jrm.dto.prueba.PruebaResponseDto;
import com.jrm.model.Item;
import com.jrm.model.Prueba;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConverterPrueba {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        // Configurar mappings específicos para evitar conflictos
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setAmbiguityIgnored(true);

        // Mapeo de Item a ItemResponseDto
        modelMapper.createTypeMap(Item.class, ItemResponseDto.class)
            .addMapping(Item::getId, ItemResponseDto::setId)
            .addMapping(Item::getDescription, ItemResponseDto::setDescription)
            .addMapping(Item::getWeight, ItemResponseDto::setWeight)
            .addMapping(Item::getPercentage, ItemResponseDto::setPercentage)
            .addMapping(src -> src.getPrueba().getId(), ItemResponseDto::setPrueba);

        // Mapeo de Prueba a PruebaResponseDto (ignorar items para evitar recursividad)
        modelMapper.createTypeMap(Prueba.class, PruebaResponseDto.class)
            .addMapping(Prueba::getId, PruebaResponseDto::setId)
            .addMapping(Prueba::getEnunciado, PruebaResponseDto::setEnunciado)
            .addMapping(Prueba::getMaxScore, PruebaResponseDto::setMaxScore)
            .addMapping(src -> src.getSpecialty().getName(), PruebaResponseDto::setSpecialtyName)
            .addMapping(Prueba::getPdfUrl, PruebaResponseDto::setPdfUrl)
            .setPostConverter(context -> {
                Prueba source = context.getSource();
                PruebaResponseDto destination = context.getDestination();
                // Mapear items manualmente
                destination.setItems(source.getItems().stream()
                    .map(item -> modelMapper.map(item, ItemResponseDto.class))
                    .toList());
                return destination;
            });
    }

    public <T, S> S genericConvert(T entity, Class<S> resultClass) {
        return modelMapper.map(entity, resultClass);
    }
}