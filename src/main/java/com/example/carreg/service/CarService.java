package com.example.carreg.service;

import com.example.carreg.converter.CarConverter;
import com.example.carreg.converter.ReservationConverter;
import com.example.carreg.domain.CarModel;
import com.example.carreg.entity.Car;
import com.example.carreg.repository.CarRepository;
import com.example.carreg.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.carreg.exception.Messages.CAR_WITH_IDENTIFIER_NOT_FOUND;
import static com.example.carreg.exception.Messages.CAR_WITH_IDENTIFIER_REMOVED;
import static com.example.carreg.exception.Messages.CAR_WITH_IDENTIFIER_UPDATED;
import static com.example.carreg.exception.Messages.NO_CARS_WITH_IDENTIFIER_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class CarService {

    private final ReservationRepository reservationRepository;

    private final CarRepository carRepository;

    private final CarConverter carConverter;

    private final ReservationConverter reservationConverter;

    /**
     * Adds car to the list.
     * @param carModel
     * @return Added {@link Car}
     */
    public CarModel addCar(CarModel carModel) //throws Exception
    {

        Car car = carConverter.convertCarModelToCar(carModel);
        //if (carRepository.findByIdentifier(carModel.getIdentifier()) != null) {
            //String message = String.format("Not unique identifier value [%s]", carModel.getIdentifier());
            //log.error(message);
            //throw new IllegalStateException(message);
        //}
        Car created = carRepository.save(car);
        log.info("Car with id [{}] has been added.", car.getIdentifier());

        return carConverter.convertCarToCarModel(created);
    }

    @Transactional(rollbackFor = Exception.class)
    public CarModel updateCar(String identifier, CarModel carModel) throws IllegalStateException {
        Car foundCar = carRepository.findByIdentifier(identifier);
        String message = Strings.EMPTY;
        if (foundCar != null) {
            foundCar.setIdentifier(carModel.getIdentifier());
            foundCar.setMake(carModel.getMake());
            foundCar.setModel(carModel.getModel());
            carRepository.save(foundCar);
            message = String.format(CAR_WITH_IDENTIFIER_UPDATED, identifier, foundCar.getIdentifier());

        } else {
            message = String.format(CAR_WITH_IDENTIFIER_NOT_FOUND, identifier);
            throw new IllegalStateException(message);
        }
        log.info(message);
        return carConverter.convertCarToCarModel(foundCar);
    }

    /**
     * Removes car from the list.
     * @param identifier
     * @return String message
     */
    @Transactional(rollbackFor = Exception.class)
    public String removeCar(String identifier) throws IllegalStateException {
        String message = Strings.EMPTY;
        Car car = carRepository.findByIdentifier(identifier);
        if (car == null) {
            message = String.format(NO_CARS_WITH_IDENTIFIER_FOUND, identifier);
            throw new IllegalStateException(message);
        } else {
            reservationRepository.removeReservationsByCar(car);
            carRepository.delete(car);
            message = String.format(CAR_WITH_IDENTIFIER_REMOVED, identifier);
        }

        log.info(message);
        return message;
    }

    /**
     * Gets all cars.
     * @return
     */
    public List<CarModel> getAllCars() {
        List<CarModel> carModels = new ArrayList<>();

        carRepository.findAll().stream().forEach(car -> carModels.add(carConverter.convertCarToCarModel(car)));

        return carModels;
    }


}
