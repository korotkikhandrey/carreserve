package com.example.carreg.service;

import com.example.carreg.entity.Car;
import com.example.carreg.repository.CarRepository;
import com.example.carreg.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.carreg.exception.Messages.CAR_WITH_LICENSE_PLATE_NOT_FOUND;
import static com.example.carreg.exception.Messages.CAR_WITH_LICENSE_PLATE_REMOVED;
import static com.example.carreg.exception.Messages.CAR_WITH_LICENSE_PLATE_UPDATED;
import static com.example.carreg.exception.Messages.NO_CARS_WITH_LICENSE_PLATE_FOUND;

/**
 * Service for {@link Car} actions (add, update, remove).
 */
@Service
@Slf4j
@AllArgsConstructor
public class CarService {

    private final ReservationRepository reservationRepository;

    private final CarRepository carRepository;

    /**
     * Adds car to the list.
     * @param car
     * @return Added {@link Car}
     */
    public Car addCar(Car car) {
        carRepository.save(car);
        log.info("Car with id [{}] has been added.", car.getLicensePlate());
        return car;
    }


    @Transactional(rollbackFor = Exception.class)
    public String updateCar(String plateLicense, Car car) {
        Car foundCar = carRepository.findByLicensePlate(plateLicense);
        String message = Strings.EMPTY;
        if (foundCar != null) {
            foundCar.setLicensePlate(car.getLicensePlate());
            foundCar.setMake(car.getMake());
            foundCar.setModel(car.getModel());
            carRepository.save(foundCar);
            message = String.format(CAR_WITH_LICENSE_PLATE_UPDATED, plateLicense, foundCar.getLicensePlate());

        } else {
            message = String.format(CAR_WITH_LICENSE_PLATE_NOT_FOUND, plateLicense);
        }
        log.info(message);
        return message;
    }

    /**
     * Removes car from the list.
     * @param licensePlate
     * @return String message
     */
    @Transactional(rollbackFor = Exception.class)
    public String removeCar(String licensePlate) {
        String message = Strings.EMPTY;
        Car car = carRepository.findByLicensePlate(licensePlate);
        if (car == null) {
            message = String.format(NO_CARS_WITH_LICENSE_PLATE_FOUND, licensePlate);
        } else {
            reservationRepository.removeReservationsByCar(car);
            carRepository.delete(car);
            message = String.format(CAR_WITH_LICENSE_PLATE_REMOVED, licensePlate);
        }

        log.info(message);
        return message;
    }

    /**
     * Gets all cars.
     * @return
     */
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

}
