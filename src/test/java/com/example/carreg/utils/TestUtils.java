package com.example.carreg.utils;

import com.example.carreg.entity.Car;
import com.example.carreg.entity.Reservation;

import java.time.LocalDateTime;

public class TestUtils {

    public static Reservation createReservation(Car car, LocalDateTime start, LocalDateTime end) {
        return new Reservation(car, start, end);
    }

    public static Car createCar(String id, String make, String model) {
        return new Car(id, make, model);
    }

}
