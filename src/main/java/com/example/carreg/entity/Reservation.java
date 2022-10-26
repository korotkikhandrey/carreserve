package com.example.carreg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.core.annotation.Order;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@Entity
@Table(name = "reservation")
@NoArgsConstructor
public class Reservation extends AbstractEntity {

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "car_id")
    @NotNull
    private Car car;

    @Column(name="start_date")
    @NotNull
    private LocalDateTime startDate;

    @Column(name="end_date")
    @NotNull
    private LocalDateTime endDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(car, that.car) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car, startDate, endDate);
    }
}
