package com.example.carreg.service;

import com.example.carreg.dto.DateRangeDto;
import com.example.carreg.entity.Car;
import com.example.carreg.entity.Reservation;
import com.example.carreg.exception.Validation;
import com.example.carreg.repository.CarRepository;
import com.example.carreg.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.carreg.exception.Messages.NOT_REGISTERED_CAR;
import static com.example.carreg.exception.Messages.RESERVATION_24_2;
import static com.example.carreg.exception.Messages.RESERVATION_END_BEFORE_START;
import static com.example.carreg.exception.Messages.RESERVATION_OVERLAP;
import static com.example.carreg.utils.CarRegUtils.isThereOverlap;

/**
 * Service for {@link Reservation} actions : adding and getting all list.
 */
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;

    public Reservation addReservation(Reservation reservation) {
        log.info("Reservation started...");
        Validation validation = checkRegistration(reservation, carRepository.findAll());
        if (CollectionUtils.isEmpty(validation.getErrors())) {
            reservationRepository.save(reservation);
            log.info("Reservation has been successfully done for object [{}]", reservation);
            return reservation;
        } else {
            log.error(String.join("\\n", validation.getErrors()));
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
    protected Validation checkRegistration(Reservation reservation, List<Car> allCars) {
        Validation validation = new Validation();

        if (!LocalDateTime.now().plusHours(24).isBefore(reservation.getStartDate()) || reservation.getEndDate().minusHours(2).isAfter(reservation.getStartDate())) {
            validation.getErrors().add(String.format(RESERVATION_24_2, reservation));
            return validation;
        }

        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            validation.getErrors().add(String.format(RESERVATION_END_BEFORE_START, reservation));
            return validation;
        }

        List<Reservation> reservationList = reservationRepository.findAll();
        if (allCars.contains(reservation.getCar())) {
            if (!reservationList.stream().noneMatch(res ->
                    isThereOverlap(new DateRangeDto(res.getStartDate(), res.getEndDate()),
                                   new DateRangeDto(reservation.getStartDate(), reservation.getEndDate()))
                    && res.getCar().equals(reservation.getCar()))) {
                validation.getErrors().add(String.format(RESERVATION_OVERLAP, reservation));
            }
        } else {
            validation.getErrors().add(String.format(NOT_REGISTERED_CAR, reservation));
        }

        return validation;
    }

    /**
     * Gets all reservations.
     * @return
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

}
