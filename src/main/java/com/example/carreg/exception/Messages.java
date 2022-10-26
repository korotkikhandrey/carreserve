package com.example.carreg.exception;

public class Messages {

    public static String RESERVATION_24_2 = "Reservation [%s] has not been done, because reservation start date time is before" +
            " now + 24hrs or duration is longer than 2 hrs.";

    public static String RESERVATION_END_BEFORE_START = "Reservation [%s] has not been done, because end time is before start time.";

    public static String RESERVATION_OVERLAP = "Reservation [%s] is overlapping another reservations. \\n Please choose another time period and/or car.";

    public static String NOT_REGISTERED_CAR = "Car [%s] has not been registered yet.";
    public static String NO_CARS_WITH_LICENSE_PLATE_FOUND = "No cars with license plate [%s].";
    public static String CAR_WITH_LICENSE_PLATE_REMOVED = "Car with licensePlate [%s] has been removed.";
    public static String CAR_WITH_LICENSE_PLATE_UPDATED = "Car with license plate [{}] has been updated. New license plate is [{}]";
    public static String CAR_WITH_LICENSE_PLATE_NOT_FOUND_ADDED = "Car with license plate [{}] not found, has been added.";








}
