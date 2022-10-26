package com.example.carreg.repository;

import com.example.carreg.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link Car}
 */
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * Finds {@link Car} by license plate.
     * @param licensePlate
     * @return
     */
    Car findByLicensePlate(String licensePlate);

    /**
     * Finds {@link Car} by make and model.
     * @param make
     * @param model
     * @return
     */
    Car findByMakeAndModel(String make, String model);

}
