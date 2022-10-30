package com.example.carreg.converter;

import com.example.carreg.domain.ReservationModel;
import com.example.carreg.entity.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReservationConverter {

    private final CarConverter carConverter;

    public Reservation convertReservationModelToReservation(ReservationModel reservationModel) {
        Reservation reservation = new Reservation();
        reservation.setCar(carConverter.convertCarModelToCar(reservationModel.getCarModel()));
        reservation.setStartDate(reservationModel.getStartDate());
        reservation.setEndDate(reservationModel.getEndDate());
        return reservation;
    }

    public ReservationModel convertReservationToReservationModel(Reservation reservation) {
        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setCarModel(carConverter.convertCarToCarModel(reservation.getCar()));
        reservationModel.setStartDate(reservation.getStartDate());
        reservationModel.setEndDate(reservation.getEndDate());
        return reservationModel;
    }

}
