package com.epam.brest.courses.service.mappers;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.model.dto.RentDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class RentMapperTest {

    @Test
    public void shouldMapRentToRentDto() {

        Rent rent = new Rent();
        rent.setRentId(1);
        rent.setClient("Client");
        rent.setRentDate(LocalDate.now());

        Dress dress = new Dress();
        dress.setDressId(1);
        dress.setDressName("Dress");
        rent.setDress(dress);

        RentDto rentDto = RentMapper.INSTANCE.rentToRentDto(rent);

        assertThat(rentDto).isNotNull();
        assertThat(rentDto.getRentId()).isEqualTo(rent.getRentId());
        assertThat(rentDto.getClient()).isEqualTo(rent.getClient());
        assertThat(rentDto.getRentDate()).isEqualTo(rent.getRentDate());
        assertThat(rentDto.getDressName()).isEqualTo(rent.getDress().getDressName());
    }

    @Test
    public void shouldMapRentDtoToRent() {

        RentDto rentDto = new RentDto();
        rentDto.setRentId(1);
        rentDto.setClient("Client");
        rentDto.setRentDate(LocalDate.now());

        Rent rent = RentMapper.INSTANCE.rentDtoToRent(rentDto);

        assertThat(rent).isNotNull();
        assertThat(rent.getRentId()).isEqualTo(rentDto.getRentId());
        assertThat(rent.getClient()).isEqualTo(rentDto.getClient());
        assertThat(rent.getRentDate()).isEqualTo(rentDto.getRentDate());
    }

}
