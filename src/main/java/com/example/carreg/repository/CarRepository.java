package com.example.carreg.repository;

import com.example.carreg.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link Car}
 */
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * Finds {@link Car} by identifier.
     * @param identifier
     * @return
     */
    Car findByIdentifier(String identifier);
}
