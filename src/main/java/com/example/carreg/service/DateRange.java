package com.example.carreg.service;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Class for handling the intervals.
 */
@Data
public class DateRange {
    private LocalDateTime start, stop ;

    public DateRange (LocalDateTime start, LocalDateTime stop) {
        if(stop.isBefore(start)) {
            throw new IllegalArgumentException ("The stop date is before the start date." );
        }
        this.start = start;
        this.stop = stop;
    }

    @Override
    public String toString () {
        return "DateRange{ " + "start=" + start + ", stop=" + stop + " }";
    }

}
