package com.example.carreg.service;

import com.example.carreg.data.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class ReservationService {

    private Set<Reservation> reservationSet = new HashSet<>();

    private final CarService carService;

    public ReservationService(CarService carService) {
        this.carService = carService;
    }

    public synchronized Reservation addReservation(Reservation reservation) {

        if (checkRegistration(reservation)) {
            reservationSet.add(reservation);
            return reservation;
        } else {
            throw new IllegalStateException("Reservation cannot be added!");
        }

    }

    private synchronized boolean checkRegistration(Reservation reservation) {
        carService.getAllCars();
        LocalDateTime minStartReservationDate = reservation.getStart().plusHours(24);
        LocalDateTime maxEndReservationDate = minStartReservationDate.plusHours(2);
        if (!LocalDateTime.now().plusHours(24).isBefore(reservation.getStart()) || reservation.getEnd().minusHours(2).isAfter(reservation.getStart())) {
            return false;
        }

        if (reservation.getStart().isAfter(reservation.getEnd())) {
            return false;
        }

        if (carService.getAllCars().contains(reservation.getCar())) {
            if (reservationSet.stream().noneMatch(res -> res.getCar().equals(reservation.getCar()))) {
                return true;
            }

            return reservationSet.stream().noneMatch(res ->
                        minStartReservationDate.isAfter(res.getStart())
                             && minStartReservationDate.isBefore(res.getEnd())
                                    && reservation.getEnd().isAfter(res.getStart())
                                        && reservation.getEnd().isBefore(res.getEnd()));
        } else {
            return false;
        }
    }

    public Set<Reservation> getAllReservations() {
        return reservationSet;
    }

}
