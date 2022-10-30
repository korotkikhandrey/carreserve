package com.example.carreg.domain;

import com.example.carreg.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Domain model for Reservation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationModel {

    @NotNull
    private CarModel carModel;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

}
