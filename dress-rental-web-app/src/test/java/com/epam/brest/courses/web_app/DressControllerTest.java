package com.epam.brest.courses.web_app;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.dto.DressDto;
import com.epam.brest.courses.service_api.DressService;
import com.epam.brest.courses.service_api.dto.DressDtoService;
import com.epam.brest.courses.web_app.controller.DressController;
import com.epam.brest.courses.web_app.validators.DressValidator;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.courses.constants.DressConstants.DRESS_NAME_SIZE;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DressController.class)
class DressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String DRESSES_ENDPOINT = "/dresses";

    @MockBean
    private DressService dressService;

    @MockBean
    private DressDtoService dressDtoService;

    @Autowired
    private ApplicationContext applicationContext;

    @TestConfiguration
    static class AdditionalConfig {
        @Bean
        public DressValidator dressValidator() {
            return new DressValidator();
        }
    }

    @Test
    void shouldReturnDressPage() throws Exception {
        DressDto dress1 = new DressDto();
        dress1.setDressId(1);
        dress1.setDressName("Dress 1");
        dress1.setNumberOfOrders(2);
        DressDto dress2 = new DressDto();
        dress2.setDressId(2);
        dress2.setDressName("Dress 2");
        dress2.setNumberOfOrders(0);
        List<DressDto> dresses = Arrays.asList(dress1, dress2);
        Mockito.when(dressDtoService.findAllWithNumberOfOrders()).thenReturn(dresses);

        mockMvc.perform(get(DRESSES_ENDPOINT)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("dresses"))
                .andExpect(model().attribute("dresses", hasItem(
                        allOf(
                                hasProperty("dressId", is(dress1.getDressId())),
                                hasProperty("dressName", is(dress1.getDressName())),
                                hasProperty("numberOfOrders", is(dress1.getNumberOfOrders()))
                        )
                )))
                .andExpect(model().attribute("dresses", hasItem(
                        allOf(
                                hasProperty("dressId", is(dress2.getDressId())),
                                hasProperty("dressName", is(dress2.getDressName())),
                                hasProperty("numberOfOrders", is(dress2.getNumberOfOrders()))
                        )
                )));
    }

    @Test
    void shouldGotoAddDressPage() throws Exception {

        mockMvc.perform(get(DRESSES_ENDPOINT + "/new")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("dress"))
                .andExpect(model().attribute("isNew", true))
                .andExpect(model().attribute("dress", isA(Dress.class)));
    }

    @Test
    void shouldGotoEditDressPage() throws Exception {
        Dress dress = new Dress();
        Integer id = 1;
        dress.setDressId(id);
        dress.setDressName("Dress 1");
        Mockito.when(dressService.findById(id)).thenReturn(Optional.of(dress));

        mockMvc.perform(get(DRESSES_ENDPOINT + "/" + id)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("dress"))
                .andExpect(model().attribute("dress", hasProperty("dressId", is(dress.getDressId()))))
                .andExpect(model().attribute("dress", hasProperty("dressName", is(dress.getDressName()))));
    }

    @Test
    void shouldRedirectToDressPageIfDressMotFoundById() throws Exception {
        Integer id = 999;
        Mockito.when(dressService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(DRESSES_ENDPOINT + "/" + id)).andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/dresses"));
    }

    @Test
    void shouldUpdateDressAfterEdit() throws Exception {
        Dress dress = new Dress();
        dress.setDressId(1);
        dress.setDressName("Dress 1");
        Mockito.when(dressService.update(dress)).thenReturn(1);

        mockMvc.perform(post(DRESSES_ENDPOINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("dressId", dress.getDressId().toString())
                .param("dressName", dress.getDressName())
                .sessionAttr("dress", dress)
        ).andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/dresses"));
    }

    @Test
    void shouldReturnDressEditPageIfUpdateDressAfterEditHasErrors() throws Exception {
        Dress dress = new Dress();
        dress.setDressId(1);
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE + 1));

        mockMvc.perform(post(DRESSES_ENDPOINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("dressId", dress.getDressId().toString())
                .param("dressName", dress.getDressName())
                .sessionAttr("dress", dress)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("dress"))
                .andExpect(model().attribute("dress", hasProperty("dressId", is(dress.getDressId()))))
                .andExpect(model().attribute("dress", hasProperty("dressName", is(dress.getDressName()))))
                .andExpect(model().hasErrors());
    }

    @Test
    void shouldCreateDress() throws Exception {
        Dress dress = new Dress();
        dress.setDressName("Dress 1");
        Mockito.when(dressService.create(dress)).thenReturn(1);


        mockMvc.perform(post(DRESSES_ENDPOINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("dressName", dress.getDressName())
                .sessionAttr("dress", dress)
        ).andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/dresses"));
    }

    @Test
    void shouldReturnDressAddPageIfCreatedDressHasErrors() throws Exception {
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE + 1));

        mockMvc.perform(post(DRESSES_ENDPOINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("dressName", dress.getDressName())
                .sessionAttr("dress", dress)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("dress"))
                .andExpect(model().attribute("isNew", true))
                .andExpect(model().attribute("dress", hasProperty("dressName", is(dress.getDressName()))))
                .andExpect(model().hasErrors());
    }

    @Test
    void shouldDeleteDress() throws Exception {
        Integer id = 1;
        Mockito.when(dressService.isDressHasRents(id)).thenReturn(false);
        Mockito.when(dressService.delete(id)).thenReturn(1);

        mockMvc.perform(get(DRESSES_ENDPOINT + "/delete/" + id)).andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/dresses"));
    }

    @Test
    void shouldShowWarningThenDeleteDressWithRents() throws Exception {
        DressDto dress1 = new DressDto();
        dress1.setDressId(1);
        dress1.setDressName("Dress 1");
        dress1.setNumberOfOrders(2);
        DressDto dress2 = new DressDto();
        dress2.setDressId(2);
        dress2.setDressName("Dress 2");
        dress2.setNumberOfOrders(0);
        List<DressDto> dresses = Arrays.asList(dress1, dress2);

        Integer id = 1;
        Mockito.when(dressService.isDressHasRents(id)).thenReturn(true);
        Mockito.when(dressDtoService.findAllWithNumberOfOrders()).thenReturn(dresses);

        mockMvc.perform(get(DRESSES_ENDPOINT + "/delete/" + id)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("dresses"))
                .andExpect(model().attribute("dresses", dresses))
                .andExpect(model().attribute("removalProhibited", true));
    }

}
