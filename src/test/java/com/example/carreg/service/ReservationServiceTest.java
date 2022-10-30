package com.example.carreg.service;

import com.example.carreg.converter.CarConverter;
import com.example.carreg.converter.ReservationConverter;
import com.example.carreg.domain.CarModel;
import com.example.carreg.domain.ReservationModel;
import com.example.carreg.entity.Car;
import com.example.carreg.entity.Reservation;
import com.example.carreg.repository.CarRepository;
import com.example.carreg.repository.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.example.carreg.exception.Messages.NOT_REGISTERED_CAR;
import static com.example.carreg.exception.Messages.RESERVATION_2;
import static com.example.carreg.exception.Messages.RESERVATION_24;
import static com.example.carreg.exception.Messages.RESERVATION_END_BEFORE_START;
import static com.example.carreg.exception.Messages.RESERVATION_OVERLAP;
import static com.example.carreg.utils.TestUtils.createCar;
import static com.example.carreg.utils.TestUtils.createCarModel;
import static com.example.carreg.utils.TestUtils.createReservation;
import static com.example.carreg.utils.TestUtils.createReservationModel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ReservationService}
 */
@ExtendWith(SpringExtension.class)
public class ReservationServiceTest {

    @MockBean
    private CarRepository carRepository;

    @MockBean
    private CarService carService;

    @MockBean
    private ReservationRepository reservationRepository;

    @SpyBean
    private ReservationService reservationService;

    @SpyBean
    private ReservationConverter reservationConverter;

    @SpyBean
    private CarConverter carConverter;

    @Test
    public void test_addReservation() {

        //given
        CarModel carModel = createCarModel("C764375", "Ford", "Focus");
        Car car = createCar("C764375", "Ford", "Focus");
        car.setId(1L);
        car.setCreateDate(LocalDateTime.now());
        car.setModifyDate(LocalDateTime.now());
        ReservationModel reservationModel = createReservationModel(carModel, LocalDateTime.now().plusHours(24).plusMinutes(1), LocalDateTime.now().plusHours(26).minusMinutes(1));
        Reservation reservation = reservationConverter.convertReservationModelToReservation(reservationModel);
        when(carService.getAllCars()).thenReturn(List.of(carModel));
        when(carRepository.findByIdentifier(anyString())).thenReturn(car);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        //when
        ReservationModel actualReservationModel = reservationService.addReservation(reservationModel);

        //then
        assertNotNull(actualReservationModel);
        assertEquals(actualReservationModel, reservationModel);
    }

    @Test
    public void test_addReservation_reservTimeBefore24hrs() {

        //given
        CarModel carModel = createCarModel("C764375", "Ford", "Focus");
        Car car = createCar("C764375", "Ford", "Focus");
        ReservationModel reservation = createReservationModel(carModel, LocalDateTime.now().plusHours(24), LocalDateTime.now().plusHours(26));
        when(carRepository.findAll()).thenReturn(List.of(car));

        //when
        IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
            ReservationModel actualReservation = reservationService.addReservation(reservation);
        });

        //then
        assertNotNull(thrown);
        assertTrue(thrown.getMessage().contains(String.format(RESERVATION_24, reservation)));

    }

    @Test
    public void test_addReservation_reservTimeLonger2Hrs() {

        //given
        CarModel carModel = createCarModel("C764375", "Ford", "Focus");
        Car car = createCar("C764375", "Ford", "Focus");
        ReservationModel reservation = createReservationModel(carModel, LocalDateTime.now().plusHours(24).plusMinutes(1),
                LocalDateTime.now().plusHours(27));
        when(carRepository.findAll()).thenReturn(List.of(car));

        //when
        IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
            ReservationModel actualReservation = reservationService.addReservation(reservation);
        });

        //then
        assertNotNull(thrown);
        assertTrue(thrown.getMessage().contains(String.format(RESERVATION_2, reservation)));
    }


    @Test
    public void test_addReservation_endTimeEarlierStartTime() {

        //given
        CarModel carModel = createCarModel("C764375", "Ford", "Focus");
        ReservationModel reservationModel = createReservationModel(carModel, LocalDateTime.now().plusHours(26), LocalDateTime.now().plusHours(24));
        when(carService.getAllCars()).thenReturn(List.of(carModel));

        //when
        IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
            ReservationModel actualReservationModel = reservationService.addReservation(reservationModel);
        });

        //then
        assertNotNull(thrown);
        assertTrue(thrown.getMessage().contains(String.format(RESERVATION_END_BEFORE_START, reservationModel)));

    }

    @Test
    public void test_addReservation_overlap() {

        //given
        Car car = createCar("C764375", "Ford", "Focus");
        CarModel carModel = createCarModel("C764375", "Ford", "Focus");
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation1 = createReservation(car, now.plusHours(24).plusMinutes(1), now.plusHours(26).minusMinutes(1));
        Reservation reservation2 = createReservation(car, now.plusHours(26).plusMinutes(30), now.plusHours(27));
        ReservationModel overlappingRservation = new ReservationModel(carModel, now.plusHours(25), now.plusHours(26));
        when(carService.getAllCars()).thenReturn(List.of(carModel));
        when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2));
        //when
        IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
             reservationService.addReservation(overlappingRservation);
        });

        //then
        assertNotNull(thrown);
        assertTrue(thrown.getMessage().contains(String.format(RESERVATION_OVERLAP, overlappingRservation)));
    }

    @Test
    public void test_addReservation_carNotRegistered() {

        //given
        CarModel carModel = createCarModel("C764375", "Ford", "Focus");
        ReservationModel reservation = createReservationModel(carModel, LocalDateTime.now().plusHours(25), LocalDateTime.now().plusHours(26));
        when(carService.getAllCars()).thenReturn(Collections.EMPTY_LIST);
        //when
        IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
            ReservationModel actualReservation = reservationService.addReservation(reservation);
        });

        //then
        assertNotNull(thrown);
        assertTrue(thrown.getMessage().contains(String.format(NOT_REGISTERED_CAR, carModel)));
    }
}
