package com.example.carreg.exception;

/**
 * Formatted string messages (constants).
 */
public class Messages {
    public static String RESERVATION_24 = "Reservation [%s] has not been done, because reservation start date time is before" +
            " now + 24hrs.";
    public static String RESERVATION_2 = "Reservation [%s] has not been done, because reservation duration is longer than 2 hrs.";
    public static String RESERVATION_END_BEFORE_START = "Reservation [%s] has not been done, because end time is before start time.";
    public static String RESERVATION_OVERLAP = "Reservation [%s] is overlapping another reservations. \\n Please choose another time period and/or car.";
    public static String NOT_REGISTERED_CAR = "Car [%s] has not been registered yet.";
    public static String NO_CARS_WITH_IDENTIFIER_FOUND = "No cars with identifier [%s].";
    public static String CAR_WITH_IDENTIFIER_REMOVED = "Car with identifier [%s] has been removed.";
    public static String CAR_WITH_IDENTIFIER_UPDATED = "Car with identifier [%s] has been updated. New identifier is [%s]";
    public static String CAR_WITH_IDENTIFIER_NOT_FOUND = "Car with identifier [%s] not found to be updated.";

    public static String UNIQUE_CONSTRAINT_VIOLATION = "Unique constraint violation";
}
