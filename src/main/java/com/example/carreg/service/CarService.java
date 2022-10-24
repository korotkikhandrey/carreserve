package com.example.carreg.service;

import com.example.carreg.data.Car;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class CarService {

    private Set<Car> cars = new HashSet<>();

    public synchronized Car addCar(Car car) {
        cars.add(car);
        log.info("Car with id [{}] has been added.", car.getId());
        return car;
    }

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
