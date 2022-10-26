package com.example.carreg.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Class for handling the intervals.
 */
@Data
public class DateRangeDto {
    private LocalDateTime start, stop ;

    public DateRangeDto(LocalDateTime start, LocalDateTime stop) {
        this.start = start;
        this.stop = stop;
    }

    @Override
    public String toString () {
        return "DateRange{ " + "start=" + start + ", stop=" + stop + " }";
    }

}
