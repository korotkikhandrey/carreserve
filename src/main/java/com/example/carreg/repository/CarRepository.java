package com.example.carreg.repository;

import com.example.carreg.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

    Car findByLicensePlate(String licensePlate);

    Car findByMakeAndModel(String make, String model);

}
