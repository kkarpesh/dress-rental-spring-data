package com.epam.brest.courses.web_app;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.model.dto.RentDto;
import com.epam.brest.courses.service_api.DressService;
import com.epam.brest.courses.service_api.RentService;
import com.epam.brest.courses.service_api.dto.RentDtoService;
import com.epam.brest.courses.web_app.controller.RentController;
import com.epam.brest.courses.web_app.validators.RentValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.courses.constants.RentConstants.RENT_CLIENT_SIZE;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RentController.class)
class RentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static final String RENTS_ENDPOINT = "/rents";

    @Autowired
    ApplicationContext applicationContext;

    @TestConfiguration
    static class AdditionalConfig {
        @Bean
        public RentValidator rentValidator() {
            return new RentValidator();
        }
    }

    @MockBean
    private RentService rentService;

    @MockBean
    private RentDtoService rentDtoService;

    @MockBean
    private DressService dressService;

    @Autowired
    private RentValidator rentValidator;

    @Test
    void shouldReturnDressPage() throws Exception {
        RentDto rent1 = new RentDto();
        rent1.setRentId(1);
        rent1.setClient("Client");
        rent1.setRentDate(LocalDate.now());
        rent1.setDressName("Dress");
        RentDto rent2 = new RentDto();
        rent2.setRentId(2);
        rent2.setClient("Client 2");
        rent2.setRentDate(LocalDate.now());
        rent2.setDressName("Dress 2");
        List<RentDto> rents = Arrays.asList(rent1, rent2);
        LocalDate dateFrom = LocalDate.now();
        LocalDate dateTo = LocalDate.now();
        Mockito.when(rentDtoService.findAllWIthDressNameByDate(dateFrom, dateTo)).thenReturn(rents);

        mockMvc.perform(get(RENTS_ENDPOINT)
                .param("dateFrom", dateFrom.toString())
                .param("dateTo", dateTo.toString())).andDo(print()
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("rents"))
                .andExpect(model().attribute("dateFrom", dateFrom))
                .andExpect(model().attribute("dateTo", dateTo))
                .andExpect(model().attribute("rents", hasItem(
                        allOf(
                                hasProperty("rentId", is(rent1.getRentId())),
                                hasProperty("client", is(rent1.getClient())),
                                hasProperty("rentDate", is(rent1.getRentDate())),
                                hasProperty("dressName", is(rent1.getDressName()))
                        )
                )))
                .andExpect(model().attribute("rents", hasItem(
                        allOf(
                                hasProperty("rentId", is(rent2.getRentId())),
                                hasProperty("client", is(rent2.getClient())),
                                hasProperty("rentDate", is(rent2.getRentDate())),
                                hasProperty("dressName", is(rent2.getDressName()))
                        )
                )));
    }

    @Test
    void shouldReturnDressPageWithErrorIfDateFromIsAfterDateTo() throws Exception {
        List<RentDto> rents = Arrays.asList(new RentDto(), new RentDto());
        LocalDate dateFrom = LocalDate.of(2020, 1, 1);
        LocalDate dateTo = LocalDate.of(2019, 1, 1);
        Mockito.when(rentDtoService.findAllWIthDressNameByDate(null, null)).thenReturn(rents);

        mockMvc.perform(get(RENTS_ENDPOINT)
                .param("dateFrom", dateFrom.toString())
                .param("dateTo", dateTo.toString())).andDo(print()
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("rents"))
                .andExpect(model().attribute("incorrectPeriod", true))
                .andExpect(model().attribute("dateFrom", is(emptyOrNullString())))
                .andExpect(model().attribute("dateTo", is(emptyOrNullString())))
                .andExpect(model().attribute("rents", rents));
    }

    @Test
    void shouldGotoAddRentPage() throws Exception {
        List<Dress> dresses = Arrays.asList(new Dress(), new Dress());
        Mockito.when(dressService.findAll()).thenReturn(dresses);

        mockMvc.perform(get(RENTS_ENDPOINT + "/new")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("rent"))
                .andExpect(model().attribute("isNew", true))
                .andExpect(model().attribute("rent", isA(Rent.class)))
                .andExpect(model().attribute("dresses", dresses));
    }

    @Test
    void shouldGotoEditRentPage() throws Exception {
        Rent rent = new Rent();
        Integer id = 1;
        rent.setRentId(id);
        rent.setClient("Client");
        rent.setRentDate(LocalDate.now());
        rent.setDressId(1);
        Mockito.when(rentService.findById(id)).thenReturn(Optional.of(rent));

        mockMvc.perform(get(RENTS_ENDPOINT + "/" + id)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("rent"))
                .andExpect(model().attribute("rent", hasProperty("rentId", is(rent.getRentId()))))
                .andExpect(model().attribute("rent", hasProperty("client", is(rent.getClient()))))
                .andExpect(model().attribute("rent", hasProperty("rentDate", is(rent.getRentDate()))))
                .andExpect(model().attribute("rent", hasProperty("dressId", is(rent.getDressId()))));
    }

    @Test
    void shouldRedirectToRentPageIfRentMotFoundById() throws Exception {
        Integer id = 999;
        Mockito.when(rentService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(RENTS_ENDPOINT + "/" + id)).andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/rents"));
    }

    @Test
    void shouldUpdateRentAfterEdit() throws Exception {
        Rent rent = new Rent();
        rent.setRentId(1);
        rent.setClient("Client");
        rent.setRentDate(LocalDate.now());
        rent.setDressId(1);
        Mockito.when(rentService.update(rent)).thenReturn(1);

        mockMvc.perform(post(RENTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("rentId", rent.getRentId().toString())
                .param("client", rent.getClient())
                .param("rentDate", rent.getRentDate().toString())
                .param("dressId", rent.getDressId().toString())
                .sessionAttr("rent", rent)
        ).andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/rents"));
    }

    @Test
    void shouldReturnRentEditPageIfUpdateRentAfterEditHasErrors() throws Exception {
        Rent rent = new Rent();
        rent.setRentId(1);
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE + 1));
        rent.setRentDate(LocalDate.now());
        rent.setDressId(1);

        mockMvc.perform(post(RENTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("rentId", rent.getRentId().toString())
                .param("client", rent.getClient())
                .param("rentDate", rent.getRentDate().toString())
                .param("dressId", rent.getDressId().toString())
                .sessionAttr("rent", rent)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("rent"))
                .andExpect(model().attribute("rent", hasProperty("rentId", is(rent.getRentId()))))
                .andExpect(model().attribute("rent", hasProperty("client", is(rent.getClient()))))
                .andExpect(model().attribute("rent", hasProperty("rentDate", is(rent.getRentDate()))))
                .andExpect(model().attribute("rent", hasProperty("dressId", is(rent.getDressId()))))
                .andExpect(model().hasErrors());
    }

    @Test
    void shouldCreateRent() throws Exception {
        Rent rent = new Rent();
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        rent.setRentDate(LocalDate.now());
        rent.setDressId(1);
        Mockito.when(rentService.create(rent)).thenReturn(1);


        mockMvc.perform(post(RENTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("client", rent.getClient())
                .param("rentDate", rent.getRentDate().toString())
                .param("dressId", rent.getDressId().toString())
                .sessionAttr("rent", rent)
        ).andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/rents"));
    }

    @Test
    void shouldReturnRentAddPageIfCreatedRentHasErrors() throws Exception {
        Rent rent = new Rent();
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE + 1));
        rent.setRentDate(LocalDate.now());
        rent.setDressId(1);

        mockMvc.perform(post(RENTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("client", rent.getClient())
                .param("rentDate", rent.getRentDate().toString())
                .param("dressId", rent.getDressId().toString())
                .sessionAttr("rent", rent)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("rent"))
                .andExpect(model().attribute("isNew", true))
                .andExpect(model().attribute("rent", hasProperty("rentId", is(rent.getRentId()))))
                .andExpect(model().attribute("rent", hasProperty("client", is(rent.getClient()))))
                .andExpect(model().attribute("rent", hasProperty("rentDate", is(rent.getRentDate()))))
                .andExpect(model().attribute("rent", hasProperty("dressId", is(rent.getDressId()))))
                .andExpect(model().hasErrors());
    }

    @Test
    void shouldDeleteRent() throws Exception {
        mockMvc.perform(get(RENTS_ENDPOINT + "/delete/" + 1)).andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/rents"));
    }

}