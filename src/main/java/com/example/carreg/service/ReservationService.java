package com.example.carreg.service;

import com.example.carreg.converter.ReservationConverter;
import com.example.carreg.domain.CarModel;
import com.example.carreg.domain.ReservationModel;
import com.example.carreg.dto.DateRangeDto;
import com.example.carreg.entity.Car;
import com.example.carreg.entity.Reservation;
import com.example.carreg.repository.CarRepository;
import com.example.carreg.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.carreg.exception.Messages.NOT_REGISTERED_CAR;
import static com.example.carreg.exception.Messages.RESERVATION_2;
import static com.example.carreg.exception.Messages.RESERVATION_24;
import static com.example.carreg.exception.Messages.RESERVATION_END_BEFORE_START;
import static com.example.carreg.exception.Messages.RESERVATION_OVERLAP;
import static com.example.carreg.utils.CarRegUtils.isThereOverlap;

@Service
@Slf4j
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final CarRepository carRepository;

    private final CarService carService;

    private final ReservationConverter reservationConverter;

    public ReservationModel addReservation(ReservationModel reservationModel) throws IllegalStateException {
        log.info("Reservation started...");
        ReservationModel result;
        String validationError = checkRegistration(reservationModel, carService.getAllCars());
        if (Strings.isEmpty(validationError)) {
            Reservation reservation = reservationConverter.convertReservationModelToReservation(reservationModel);
            Car car = carRepository.findByIdentifier(reservation.getCar().getIdentifier());
            reservation.setCar(car);
            Reservation savedReservation = reservationRepository.save(reservation);
            result = reservationConverter.convertReservationToReservationModel(savedReservation);
            log.info("Reservation has been successfully done for object [{}]", reservation);
        } else {
            log.error(validationError);
            throw new IllegalStateException("Reservation cannot be added. Reason(s): " + validationError);
        }

        return result;
    }

    /**
     * Checks reservation: date range for reservation should be made later than 24 hours
     * and duration should be exceeding 2 hours. Car should be available for the time period, obviously.
     * All the errors are collected in list
     * @param reservation
     * @return list of errors
     */
    protected String checkRegistration(ReservationModel reservation, List<CarModel> allCars) {
        String validationError = Strings.EMPTY;

        if (reservation.getStartDate().isBefore(LocalDateTime.now().plusHours(24))) {
            validationError = String.format(RESERVATION_24, reservation);
            return validationError;
        }

        if (reservation.getEndDate().minusHours(2).isAfter(reservation.getStartDate())) {
            validationError = String.format(RESERVATION_2, reservation);
            return validationError;
        }

        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            validationError = String.format(RESERVATION_END_BEFORE_START, reservation);
            return validationError;
        }

        if (!allCars.contains(reservation.getCarModel())) {
            validationError = String.format(NOT_REGISTERED_CAR, reservation.getCarModel());
            return validationError;
        }

        if (reservationRepository.findAll().stream().anyMatch(res ->
                isThereOverlap(new DateRangeDto(res.getStartDate(), res.getEndDate()),
                        new DateRangeDto(reservation.getStartDate(), reservation.getEndDate())))) {
            validationError = String.format(RESERVATION_OVERLAP, reservation);
            return validationError;
        }

        return validationError;
    }

    /**
     * Gets all reservations.
     * @return
     */
    public List<ReservationModel> getAllReservations() {
        List<ReservationModel> reservationModels = new ArrayList<>();
        reservationRepository
                .findAll().stream().forEach(reservation -> reservationModels.add(
                        reservationConverter.convertReservationToReservationModel(reservation)));

        return reservationModels;
    }

}
