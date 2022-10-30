package com.example.carreg.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link DateRangeDto}
 */
public class DateRangeDtoTest {

    @Test
    public void test_constructor_endDateBeforeStartDate() {


        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DateRangeDto dateRangeDto = new DateRangeDto(LocalDateTime.now().plusHours(1), LocalDateTime.now());
        });

        assertEquals("The stop date is before the start date.", thrown.getMessage());
    }

    @Test
    public void test_toString() {

        LocalDateTime start = LocalDateTime.of(2022, 10, 1, 2, 54);
        LocalDateTime end = start.plusHours(1);

        DateRangeDto dateRangeDto = new DateRangeDto(start, end);

        assertEquals(dateRangeDto.toString(), "DateRange{ start=2022-10-01T02:54, stop=2022-10-01T03:54 }");
    }
}
