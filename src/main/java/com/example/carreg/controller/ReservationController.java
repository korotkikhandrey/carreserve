package com.example.carreg.controller;

import com.example.carreg.domain.ReservationModel;
import com.example.carreg.entity.Reservation;
import com.example.carreg.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Reservation restcontroller.
 */
@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Adds reservation for particular car.
     * @param reservationModel
     * @return ResponseEntity with {@link Reservation} saved.
     */
    @PostMapping(value = "/add",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationModel> addReservation(@RequestBody ReservationModel reservationModel) {
        ResponseEntity responseEntity;
        try {
            ReservationModel reservationDone = reservationService.addReservation(reservationModel);
            responseEntity = new ResponseEntity<>(reservationDone, HttpStatus.OK);
        } catch (Exception exception) {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }

        return responseEntity;
    }

    /**
     * Gets all reservations from a system.
     * @param
     * @return ResponseEntity with List of {@link Reservation} saved.
     */
    @GetMapping(value = "/all",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReservationModel>> getAllReservations() {
        List<ReservationModel> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

}
