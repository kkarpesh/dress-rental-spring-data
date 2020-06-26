package com.epam.brest.courses.service.mappers;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.dto.DressDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DressMapper {

    DressMapper INSTANCE = Mappers.getMapper(DressMapper.class);

    @Mappings({
            @Mapping(source = "dressId", target = "dressId"),
            @Mapping(source = "dressName", target = "dressName"),
            @Mapping(target = "numberOfOrders",
                    expression = "java(dress.getRents().size())")
    })
    DressDto dressToDressDto(Dress dress);

    @Mappings({
            @Mapping(source = "dressId", target = "dressId"),
            @Mapping(source = "dressName", target = "dressName")
    })
    Dress dressDtoToDress(DressDto dressDto);
}
