package com.example.carreg.converter;

import com.example.carreg.domain.CarModel;
import com.example.carreg.domain.ReservationModel;
import com.example.carreg.entity.Car;
import com.example.carreg.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.example.carreg.utils.TestUtils.createCar;
import static com.example.carreg.utils.TestUtils.createCarModel;
import static com.example.carreg.utils.TestUtils.createReservation;
import static com.example.carreg.utils.TestUtils.createReservationModel;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for {@link CarConverter}
 */
public class ReservationConverterTest {

    private ReservationConverter reservationConverter;
    private CarConverter carConverter;

    @BeforeEach
    public void setup() {
        carConverter = new CarConverter();
        reservationConverter = new ReservationConverter(carConverter);
    }

    @Test
    public void test_convertReservationModelToReservation() {
        CarModel carModel = createCarModel("C764375", "Ford", "Focus");
        Car car = createCar(carModel.getIdentifier(), carModel.getMake(), carModel.getModel());
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        ReservationModel reservationModel = createReservationModel(carModel, start, end);
        Reservation reservation = reservationConverter.convertReservationModelToReservation(reservationModel);
        assertEquals(car, reservation.getCar());
        assertEquals(reservationModel.getStartDate(), reservation.getStartDate());
        assertEquals(reservationModel.getEndDate(), reservation.getEndDate());
    }

    @Test
    public void test_convertCarToCarModel() {
        CarModel carModel = createCarModel("C764375", "Ford", "Focus");
        Car car = createCar(carModel.getIdentifier(), carModel.getMake(), carModel.getModel());
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        Reservation reservation = createReservation(car, start, end);
        ReservationModel reservationModel = reservationConverter.convertReservationToReservationModel(reservation);
        assertEquals(carModel, reservationModel.getCarModel());
        assertEquals(reservation.getStartDate(), reservationModel.getStartDate());
        assertEquals(reservation.getEndDate(), reservationModel.getEndDate());
    }

}
