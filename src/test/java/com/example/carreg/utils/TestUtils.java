package com.example.carreg.utils;

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

    /**
     * Creates {@link  Car} for test purposes.
     * @param id
     * @param make
     * @param model
     * @return {@link Car}
     */
    public static Car createCar(String id, String make, String model) {
        return new Car(id, make, model);
    }

}
