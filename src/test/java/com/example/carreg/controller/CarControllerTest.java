package com.example.carreg.controller;

import com.example.carreg.data.Car;
import com.example.carreg.service.CarService;
import com.example.carreg.service.ReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.carreg.utils.TestUtils.createCar;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest
public class CarControllerTest {

    @SpyBean
    private ReservationService reservationService;

    @SpyBean
    private ReservationController reservationController;

    @SpyBean
    private CarService carService;

    @SpyBean
    private CarController carController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private ObjectWriter objectWriter;

    private Car car;

    @BeforeEach
    public void setupBefore() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JSR310Module());
        this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectWriter = this.mapper.writer().withDefaultPrettyPrinter();
        car = createCar("C764375", "Ford", "Focus");
        carService.getAllCars().clear();
    }

    @AfterEach
    public void setupAfter() {
        carService.getAllCars().clear();
    }

    @Test
    public void test_registration_car() throws Exception {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();;

        String json = objectWriter.writeValueAsString(car);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/car").content(json).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
        assertEquals(json.replaceAll("\\s", ""), mvcResult.getResponse().getContentAsString());
        assertEquals(200, status);
    }

    @Test
    public void test_update_car() throws Exception {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();;
        carService.addCar(car);
        Car carForUpdate = createCar("C764376", "Ford", "Focus");
        String json = objectWriter.writeValueAsString(carForUpdate);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/registration/car").content(json).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
        assertEquals(json.replaceAll("\\s", ""), mvcResult.getResponse().getContentAsString());
        assertEquals(200, status);
    }

    @Test
    public void test_remove_car() throws Exception {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();;
        carService.addCar(car);

        //String json = objectWriter.writeValueAsString(carForUpdate);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/registration/car/{id}", car.getId())).andReturn();

        int status = mvcResult.getResponse().getStatus();
        //assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
        //assertEquals(json.replaceAll("\\s", ""), mvcResult.getResponse().getContentAsString());
        assertEquals(200, status);
    }


}
