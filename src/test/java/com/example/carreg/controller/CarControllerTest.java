package com.example.carreg.controller;

import com.example.carreg.entity.Car;
import com.example.carreg.repository.CarRepository;
import com.example.carreg.repository.ReservationRepository;
import com.example.carreg.service.CarService;
import com.example.carreg.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.carreg.utils.TestUtils.createCar;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link CarController}
 */
@WebMvcTest
public class CarControllerTest {

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private ReservationService reservationService;

    @SpyBean
    private CarService carService;

    @MockBean
    private CarRepository carRepository;

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

    /**
     * Tests POST "/registration/car" endpoint.
     * @throws Exception
     */
    @Test
    public void test_addCar() throws Exception {

        //given
        String json = objectWriter.writeValueAsString(car);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/car").content(json).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
        assertEquals(json.replaceAll("\\s", ""), mvcResult.getResponse().getContentAsString());
        assertEquals(200, status);
    }

    /**
     * Tests PUT "/registration/car" endpoint.
     * @throws Exception
     */
    @Test
    public void test_updateCar() throws Exception {

        //given
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        when(carRepository.findByLicensePlate(anyString())).thenReturn(car);
        Car carForUpdate = createCar("C764376", "Ford", "Focus");
        String json = objectWriter.writeValueAsString(carForUpdate);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/registration/car").param("plateLicense", "C764375").content(json).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        //assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
        assertEquals(json.replaceAll("\\s", ""), mvcResult.getResponse().getContentAsString());
        assertEquals(200, status);
    }

    /**
     * Tests DELETE "/registration/car/{id}" endpoint.
     * @throws Exception
     */
    @Test
    public void test_removeCar() throws Exception {

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/registration/car/{id}", car.getLicensePlate())).andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }


}
