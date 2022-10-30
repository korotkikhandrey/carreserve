package com.example.carreg.converter;

import com.example.carreg.domain.CarModel;
import com.example.carreg.entity.Car;
import org.springframework.stereotype.Component;

@Component
public class CarConverter {

    public Car convertCarModelToCar(CarModel carModel) {
        Car car = new Car();
        car.setModel(carModel.getModel());
        car.setMake(carModel.getMake());
        car.setIdentifier(carModel.getIdentifier());
        return car;
    }

    public CarModel convertCarToCarModel(Car car) {
        CarModel carModel = new CarModel();
        carModel.setModel(car.getModel());
        carModel.setMake(car.getMake());
        carModel.setIdentifier(car.getIdentifier());
        return carModel;
    }

}
