package com.example.carreg.utils;

import com.example.carreg.dto.DateRangeDto;

/**
 * Utils class.
 */
public class CarRegUtils {

    /**
     * Checks the overlap between 2 {@link DateRangeDto}
     * @param t1
     * @param t2
     * @return true if there is overlap, false otherwise.
     */
    public static boolean isThereOverlap(DateRangeDto t1, DateRangeDto t2) {
        return !(t1.getStop().isBefore(t2.getStart()) || t1.getStart().isAfter(t2.getStop()));
    }

}
