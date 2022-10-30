package com.example.carreg.utils;

import com.example.carreg.domain.CarModel;
import com.example.carreg.domain.ReservationModel;
import com.example.carreg.entity.Car;
import com.example.carreg.entity.Reservation;

import java.time.LocalDateTime;

/**
 * Test utils class.
 */
public class TestUtils {

    /**
     * Creates {@link  Reservation} for test purposes.
     * @param car
     * @param start
     * @param end
     * @return {@link  Reservation}
     */
    public static Reservation createReservation(Car car, LocalDateTime start, LocalDateTime end) {
        return new Reservation(car, start, end);
    }

    public static ReservationModel createReservationModel(CarModel carModel, LocalDateTime start, LocalDateTime end) {
        return new ReservationModel(carModel, start, end);
    }

    /**
     * Creates {@link  Car} for test purposes.
     * @param identifier
     * @param make
     * @param model
     * @return {@link Car}
     */
    public static Car createCar(String identifier, String make, String model) {
        return new Car(identifier, make, model);
    }

    public static CarModel createCarModel(String identifier, String make, String model) {
        return new CarModel(identifier, make, model);
    }

}
