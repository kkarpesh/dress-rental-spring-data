package com.epam.brest.courses.service.mappers;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.model.dto.DressDto;
import com.epam.brest.courses.service.mappers.DressMapper;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class DressMapperTest {

    @Test
    public void shouldMapDressToDressDto() {

        Dress dress = new Dress();
        dress.setDressId(1);
        dress.setDressName("Dress");
        dress.setRents(Collections.singleton(new Rent()));

        DressDto dressDto = DressMapper.INSTANCE.dressToDressDto(dress);

        assertThat(dressDto).isNotNull();
        assertThat(dressDto.getDressId()).isEqualTo(dress.getDressId());
        assertThat(dressDto.getDressName()).isEqualTo(dress.getDressName());
        assertThat(dressDto.getNumberOfOrders()).isEqualTo(dress.getRents().size());
    }

    @Test
    public void shouldMapDressDtoToDress() {

        DressDto dressDto = new DressDto();
        dressDto.setDressId(1);
        dressDto.setDressName("Dress");

        Dress dress = DressMapper.INSTANCE.dressDtoToDress(dressDto);

        assertThat(dress).isNotNull();
        assertThat(dress.getDressId()).isEqualTo(dressDto.getDressId());
        assertThat(dress.getDressName()).isEqualTo(dressDto.getDressName());
    }
}
