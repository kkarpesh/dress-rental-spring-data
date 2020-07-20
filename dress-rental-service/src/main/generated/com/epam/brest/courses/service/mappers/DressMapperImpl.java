package com.epam.brest.courses.service.mappers;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.dto.DressDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-07-20T15:04:53-0400",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.7 (Ubuntu)"
)
@Component
public class DressMapperImpl implements DressMapper {

    @Override
    public DressDto dressToDressDto(Dress dress) {
        if ( dress == null ) {
            return null;
        }

        DressDto dressDto = new DressDto();

        dressDto.setDressId( dress.getDressId() );
        dressDto.setDressName( dress.getDressName() );

        dressDto.setNumberOfOrders( dress.getRents().size() );

        return dressDto;
    }

    @Override
    public Dress dressDtoToDress(DressDto dressDto) {
        if ( dressDto == null ) {
            return null;
        }

        Dress dress = new Dress();

        dress.setDressId( dressDto.getDressId() );
        dress.setDressName( dressDto.getDressName() );

        return dress;
    }
}
