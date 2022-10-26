package com.example.carreg.controller;

import com.example.carreg.entity.Car;
import com.example.carreg.entity.Reservation;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.example.carreg.utils.TestUtils.createCar;
import static com.example.carreg.utils.TestUtils.createReservation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest
public class ReservationControllerTest {

    @SpyBean
    private ReservationService reservationService;

    @SpyBean
    private ReservationController reservationController;

    @MockBean
    private CarService carService;

    @MockBean
    private CarRepository carRepository;

    @MockBean
    private ReservationRepository reservationRepository;

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
        reservationService.getAllReservations().clear();
    }

    /**
     * Tests that in case of registered car and adding reservation for it, it will be returned with code 200.
     * @throws Exception
     */
    @Test
    public void test_addReservation() throws Exception {

        //given
        Reservation reservation = createReservation(car, LocalDateTime.now().plusHours(24).plusMinutes(1), LocalDateTime.now().plusHours(26).minusMinutes(1));
        String json = objectWriter.writeValueAsString(reservation);
        when(carRepository.findAll()).thenReturn(List.of(car));

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/reservation/add").content(json).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
        assertEquals(json.replaceAll("\\s", ""), mvcResult.getResponse().getContentAsString());
        assertEquals(200, status);
    }

    /**
     * Tests that in case of there are some reservations in DB, all of them will be returned with code 200.
     * @throws Exception
     */
    @Test
    public void test_getAllReservations() throws Exception {

        //given
        Reservation reservation1 = createReservation(car, LocalDateTime.now().plusHours(24).plusMinutes(1), LocalDateTime.now().plusHours(26).minusMinutes(1));
        Reservation reservation2 = createReservation(car, LocalDateTime.now().plusHours(27).plusMinutes(1), LocalDateTime.now().plusHours(28).minusMinutes(1));
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);
        String json = objectWriter.writeValueAsString(reservations);
        when(reservationRepository.findAll()).thenReturn(reservations);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/reservation/all")).andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
        assertEquals(json.replaceAll("\\s", ""), mvcResult.getResponse().getContentAsString());
        assertEquals(200, status);
    }

}


