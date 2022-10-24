package com.example.carreg.service;

import com.example.carreg.data.Reservation;
import com.example.carreg.exception.Validation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.example.carreg.utils.CarRegUtils.isThereOverlap;

/**
 * Service for {@link Reservation} actions : adding and getting all list.
 */
@Service
@Slf4j
public class ReservationService {

    private Set<Reservation> reservationSet = new HashSet<>();

    private final CarService carService;

    public ReservationService(CarService carService) {
        this.carService = carService;
    }

    public synchronized Reservation addReservation(Reservation reservation) {
        log.info("Reservation started...");
        Validation validation = checkRegistration(reservation);
        if (CollectionUtils.isEmpty(validation.getErrors())) {
            reservationSet.add(reservation);
            log.debug("Reservation has been successfully done for object [{}]", reservation);
            return reservation;
        } else {
            throw new IllegalStateException("Reservation cannot be added. Reason(s): " + String.join("\\n", validation.getErrors()));
        }
    }

    /**
     * Checks reservation: date range for reservation should be made later than 24 hours
     * and duration should be exceeding 2 hours. Car should be available for the time period, obviously.
     * All the errors are collected in {@link Validation} object.
     * @param reservation
     * @return {@link Validation}
     */
    protected synchronized Validation checkRegistration(Reservation reservation) {
        Validation validation = new Validation();
        if (!LocalDateTime.now().plusHours(24).isBefore(reservation.getStart()) || reservation.getEnd().minusHours(2).isAfter(reservation.getStart())) {
            validation.getErrors().add(String.format("Reservation [%s] has not been done, because reservation start date time is before " +
                    " now + 24hrs or duration is longer than 2 hrs.", reservation));
        }

        if (reservation.getStart().isAfter(reservation.getEnd())) {
            validation.getErrors().add(String.format("Reservation [%s] has not been done, because end time is less than start time.", reservation));
        }

        if (carService.getAllCars().contains(reservation.getCar())) {
            if (!reservationSet.stream().noneMatch(res ->
                    isThereOverlap(new DateRange(res.getStart(), res.getEnd()),
                                   new DateRange(reservation.getStart(), reservation.getEnd()))
                    && res.getCar().equals(reservation.getCar()))) {
                validation.getErrors().add(String.format("Reservation [%s] is overlapping another reservations. " +
                        "Please choose another time period and/or car.", reservation));
            }
        } else {
            validation.getErrors().add(String.format("Car [%s] has not been registered yet.", reservation));
        }

        return validation;
    }

    /**
     * Gets all reservations.
     * @return
     */
    public Set<Reservation> getAllReservations() {
        return reservationSet;
    }
}
