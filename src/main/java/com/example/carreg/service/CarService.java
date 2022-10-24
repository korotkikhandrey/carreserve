package com.example.carreg.service;

import com.example.carreg.data.Car;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service for {@link Car} actions (add, update, remove).
 */
@Service
@Slf4j
public class CarService {

    private Set<Car> cars = new HashSet<>();

    /**
     * Adds car to the list.
     * @param car
     * @return Added {@link Car}
     */
    public synchronized Car addCar(Car car) {
        cars.add(car);
        log.info("Car with id [{}] has been added.", car.getId());
        return car;
    }

    /**
     * Updates car in the list. Note: only id can be updated.
     * @param car
     * @return Updated {@link Car}
     */
    public synchronized Car updateCar(Car car) {
        Car foundCar = cars.stream().filter(c -> c.getMake().equals(car.getMake())
                && c.getModel().equals(car.getModel())).findFirst().orElse(null);
        if (foundCar != null) {
            cars.remove(foundCar);
            cars.add(car);
            log.info("Car with id [{}] has been updated. New id id [{}]", foundCar.getId(), car.getId());
            return car;
        } else {
            cars.add(car);
            log.info("Car with id [{}] has been added.", foundCar.getId());
            return car;
        }
    }

    /**
     * Removes car from the list.
     * @param id
     * @return String message
     */
    public synchronized String removeCar(String id) {
        String message = Strings.EMPTY;
        Optional<Car> carOpt = cars.stream().filter(c -> c.getId().equals(id)).findFirst();
        if (carOpt.isPresent()) {
            cars.remove(carOpt.get());
            message = String.format("Car with id [%s] has been removed.", id);
            log.info(message);

        } else {
            message = String.format("No cars with [%s].", id);
            log.info(message);
        }

        return message;
    }

    public Set<Car> getAllCars() {
        return cars;
    }
}
