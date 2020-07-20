package com.epam.brest.courses.service.mappers;

import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.model.dto.RentDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-07-20T15:04:53-0400",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.7 (Ubuntu)"
)
@Component
public class RentMapperImpl implements RentMapper {

    @Override
    public RentDto rentToRentDto(Rent rent) {
        if ( rent == null ) {
            return null;
        }

        RentDto rentDto = new RentDto();

        rentDto.setClient( rent.getClient() );
        rentDto.setRentDate( rent.getRentDate() );
        rentDto.setRentId( rent.getRentId() );

        rentDto.setDressName( rent.getDress().getDressName() );

        return rentDto;
    }

    @Override
    public Rent rentDtoToRent(RentDto rentDto) {
        if ( rentDto == null ) {
            return null;
        }

        Rent rent = new Rent();

        rent.setClient( rentDto.getClient() );
        rent.setRentDate( rentDto.getRentDate() );
        rent.setRentId( rentDto.getRentId() );

        return rent;
    }
}
