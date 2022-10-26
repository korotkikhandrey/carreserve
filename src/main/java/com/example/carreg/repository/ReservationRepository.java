package com.example.carreg.repository;

import com.example.carreg.entity.Car;
import com.example.carreg.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findReservationsByCarId(Long id);

    @Modifying
    @Query("delete from Reservation r where r.car = :car")
    @Transactional
    void removeReservationsByCar(@Param("car") Car car);

}
