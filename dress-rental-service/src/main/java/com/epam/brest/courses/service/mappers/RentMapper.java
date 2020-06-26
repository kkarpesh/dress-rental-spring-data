package com.epam.brest.courses.service.mappers;

import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.model.dto.RentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RentMapper {

    RentMapper INSTANCE = Mappers.getMapper(RentMapper.class);

    @Mappings({
            @Mapping(source = "rentId", target = "rentId"),
            @Mapping(source = "client", target = "client"),
            @Mapping(source = "rentDate", target = "rentDate"),
            @Mapping(target = "dressName",
                    expression = "java(rent.getDress().getDressName())")
    })
    RentDto rentToRentDto(Rent rent);

    @Mappings({
            @Mapping(source = "rentId", target = "rentId"),
            @Mapping(source = "client", target = "client"),
            @Mapping(source = "rentDate", target = "rentDate")
    })
    Rent rentDtoToRent(RentDto rentDto);

}
