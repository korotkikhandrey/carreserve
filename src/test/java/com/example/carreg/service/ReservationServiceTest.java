package com.example.carreg.service;

import com.example.carreg.entity.Car;
import com.example.carreg.entity.Reservation;
import com.example.carreg.repository.CarRepository;
import com.example.carreg.repository.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.example.carreg.utils.TestUtils.createCar;
import static com.example.carreg.utils.TestUtils.createReservation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ReservationService}
 */
@ExtendWith(SpringExtension.class)
public class ReservationServiceTest {

    @MockBean
    private CarRepository carRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @SpyBean
    private ReservationService reservationService;

    @Test
    public void test_addReservation() {

        //given
        Car car = createCar("C764375", "Ford", "Focus");
        Reservation reservation = createReservation(car, LocalDateTime.now().plusHours(24).plusMinutes(1), LocalDateTime.now().plusHours(26).minusMinutes(1));
        when(carRepository.findAll()).thenReturn(List.of(car));

        //when
        Reservation actualReservation = reservationService.addReservation(reservation);

        //then
        assertNotNull(actualReservation);
        assertEquals(actualReservation, reservation);
    }

    @Test
    public void test_addReservation_reservTimeBefore24hrsOrLonger2Hrs() {

        //given
        Car car = createCar("C764375", "Ford", "Focus");
        Reservation reservation = createReservation(car, LocalDateTime.now().plusHours(24), LocalDateTime.now().plusHours(26));
        when(carRepository.findAll()).thenReturn(List.of(car));

        //when
        IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
            Reservation actualReservation = reservationService.addReservation(reservation);
        });

        //then
        assertTrue(thrown.getMessage().contains("because reservation start date time is before now + 24hrs or duration is longer than 2 hrs"));
    }

    @Test
    public void test_addReservation_endTimeEarlierStartTime() {

        //given
        Car car = createCar("C764375", "Ford", "Focus");
        Reservation reservation = createReservation(car, LocalDateTime.now().plusHours(26), LocalDateTime.now().plusHours(24));
        when(carRepository.findAll()).thenReturn(List.of(car));

        //when
        IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
            Reservation actualReservation = reservationService.addReservation(reservation);
        });

        //then
        assertTrue(thrown.getMessage().contains("end time is before start time"));
    }

    @Test
    public void test_addReservation_overlap() {

        //given
        Car car = createCar("C764375", "Ford", "Focus");
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation1 = createReservation(car, now.plusHours(24).plusMinutes(1), now.plusHours(26).minusMinutes(1));
        Reservation reservation2 = createReservation(car, now.plusHours(26).plusMinutes(30), now.plusHours(27));
        Reservation overlappingRservation = new Reservation(car, now.plusHours(25), now.plusHours(26));
        when(carRepository.findAll()).thenReturn(List.of(car));
        when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2));

        //when
        IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
             reservationService.addReservation(overlappingRservation);
        });

        //then
        assertTrue(thrown.getMessage().contains("overlapping another reservations"));
    }

    @Test
    public void test_addReservation_carNotRegistered() {

        //given
        Car car = createCar("C764375", "Ford", "Focus");
        Reservation reservation = createReservation(car, LocalDateTime.now().plusHours(25), LocalDateTime.now().plusHours(26));
        when(carRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
        //when
        IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
            Reservation actualReservation = reservationService.addReservation(reservation);
        });

        //then
        assertTrue(thrown.getMessage().contains("has not been registered yet"));
    }
}
