package com.example.carreg.controller;

import com.example.carreg.converter.CarConverter;
import com.example.carreg.converter.ReservationConverter;
import com.example.carreg.domain.CarModel;
import com.example.carreg.entity.Car;
import com.example.carreg.repository.CarRepository;
import com.example.carreg.repository.ReservationRepository;
import com.example.carreg.service.CarService;
import com.example.carreg.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.carreg.exception.Messages.CAR_WITH_IDENTIFIER_NOT_FOUND;
import static com.example.carreg.exception.Messages.CAR_WITH_IDENTIFIER_REMOVED;
import static com.example.carreg.exception.Messages.NO_CARS_WITH_IDENTIFIER_FOUND;
import static com.example.carreg.utils.TestStringConstants.CONTENT_TYPE_TEXT_UTF8;
import static com.example.carreg.utils.TestStringConstants.IDENTIFIER_INCORRECT;
import static com.example.carreg.utils.TestStringConstants.MAKE_EMPTY;
import static com.example.carreg.utils.TestStringConstants.MODEL_EMPTY;
import static com.example.carreg.utils.TestUtils.createCar;
import static com.example.carreg.utils.TestUtils.createCarModel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

    @MockBean
    private CarConverter carConverter;

    @MockBean
    private ReservationConverter reservationConverter;

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

        CarModel carModel = createCarModel(car.getIdentifier(), car.getMake(), car.getModel());
        String json = objectWriter.writeValueAsString(carModel);
        when(carConverter.convertCarModelToCar(any(CarModel.class))).thenReturn(car);
        when(carRepository.save(any(Car.class))).thenReturn(car);
        when(carConverter.convertCarToCarModel(any(Car.class))).thenReturn(carModel);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/car").content(json).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        //assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
        assertEquals(json.replaceAll("\\s", ""), mvcResult.getResponse().getContentAsString());
        assertEquals(200, status);
    }

    @Test
    public void test_addCar_validation_incorrect_identifier() throws Exception {

        //given
        car = createCar("T764375", "", "Focus");
        String json = objectWriter.writeValueAsString(car);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/car").content(json).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(CONTENT_TYPE_TEXT_UTF8, mvcResult.getResponse().getContentType());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(IDENTIFIER_INCORRECT));
        assertEquals(400, status);
    }

    @Test
    public void test_addCar_validation_emptyMake() throws Exception {

        //given
        car = createCar("C764375", "", "Focus");
        String json = objectWriter.writeValueAsString(car);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/car").content(json).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(CONTENT_TYPE_TEXT_UTF8, mvcResult.getResponse().getContentType());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(MAKE_EMPTY));
        assertEquals(400, status);
    }

    @Test
    public void test_addCar_validation_emptyModel() throws Exception {

        //given
        car = createCar("C764375", "Ford", "");
        String json = objectWriter.writeValueAsString(car);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/registration/car").content(json).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        int status = mockHttpServletResponse.getStatus();
        assertEquals(CONTENT_TYPE_TEXT_UTF8, mvcResult.getResponse().getContentType());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(MODEL_EMPTY));
        assertEquals(400, status);
    }

    /**
     * Tests PUT "/registration/car" endpoint.
     * @throws Exception
     */
    @Test
    public void test_updateCar() throws Exception {

        //given
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        when(carRepository.findByIdentifier(anyString())).thenReturn(car);
        CarModel carForUpdate = createCarModel("C764376", "Ford", "Focus");
        String json = objectWriter.writeValueAsString(carForUpdate);
        when(carConverter.convertCarToCarModel(any(Car.class))).thenReturn(carForUpdate);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/registration/car").param("identifier", "C764375").content(json).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        int status = mockHttpServletResponse.getStatus();

        assertTrue(json.replaceAll("\\s", "").equals(mockHttpServletResponse.getContentAsString()));
        assertEquals(200, status);
    }

    @Test
    public void test_updateCar_notFound() throws Exception {

        //given
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        when(carRepository.findByIdentifier(anyString())).thenReturn(null);
        CarModel carForUpdate = createCarModel("C764376", "Ford", "Focus");
        String json = objectWriter.writeValueAsString(carForUpdate);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/registration/car").param("identifier", "C764375").content(json).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        int status = mockHttpServletResponse.getStatus();

        assertTrue(mockHttpServletResponse.getContentAsString().
                contains(String.format(CAR_WITH_IDENTIFIER_NOT_FOUND, car.getIdentifier())));

        assertEquals(500, status);
    }

    /**
     * Tests DELETE "/registration/car/{id}" endpoint. Not found car case.
     * @throws Exception
     */
    @Test
    public void test_removeCar_notFoundCar() throws Exception {

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/registration/car/{id}", car.getIdentifier())).andReturn();

        //then
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        int status = mockHttpServletResponse.getStatus();
        //assertTrue(mockHttpServletResponse.getContentAsString().contains(NO_CARS_WITH_IDENTIFIER));
        assertTrue(mockHttpServletResponse.getContentAsString().
                contains(String.format(NO_CARS_WITH_IDENTIFIER_FOUND, car.getIdentifier())));

        assertEquals(500, status);
    }

    /**
     * Tests DELETE "/registration/car/{id}" endpoint. Found car case.
     * @throws Exception
     */
    @Test
    public void test_removeCar_foundCar() throws Exception {
        //given
        when(carRepository.findByIdentifier(anyString())).thenReturn(car);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/registration/car/{id}", car.getIdentifier())).andReturn();

        //then
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        int status = mockHttpServletResponse.getStatus();

        assertTrue(mockHttpServletResponse.getContentAsString().
                contains(String.format(CAR_WITH_IDENTIFIER_REMOVED, car.getIdentifier())));
        assertEquals(200, status);
    }

}
