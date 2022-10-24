package com.example.carreg.data;

import lombok.Data;
import org.springframework.core.annotation.Order;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class Reservation {
    private Car car;
    @Order(1)
    private LocalDateTime start;
    @Order(2)
    private LocalDateTime end;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(car, that.car) && Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car, start, end);
    }
}
