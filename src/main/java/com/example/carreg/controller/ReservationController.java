package com.example.carreg.controller;

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
     * @param reservation
     * @return ResponseEntity with {@link Reservation} saved.
     */
    @PostMapping(value = "/add",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reservation> addReservation(@RequestBody Reservation reservation) {
        Reservation reservationDone = reservationService.addReservation(reservation);
        return new ResponseEntity<>(reservationDone, HttpStatus.OK);
    }

    /**
     * Gets all reservations from a system.
     * @param
     * @return ResponseEntity with List of {@link Reservation} saved.
     */
    @GetMapping(value = "/all",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

}
