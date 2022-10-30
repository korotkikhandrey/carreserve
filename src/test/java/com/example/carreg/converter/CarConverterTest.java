package com.example.carreg.converter;

import com.example.carreg.domain.CarModel;
import com.example.carreg.entity.Car;
import org.junit.jupiter.api.Test;

import static com.example.carreg.utils.TestUtils.createCar;
import static com.example.carreg.utils.TestUtils.createCarModel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test for {@link CarConverter}
 */
public class CarConverterTest {

    CarConverter carConverter = new CarConverter();

    @Test
    public void test_convertCarModelToCar() {
        CarModel carModel = createCarModel("C764375", "Ford", "Focus");
        Car car = carConverter.convertCarModelToCar(carModel);
        assertEquals(carModel.getModel(), car.getModel());
        assertEquals(carModel.getMake(), car.getMake());
        assertEquals(carModel.getIdentifier(), car.getIdentifier());
        assertNull(car.getCreateDate());
        assertNull(car.getModifyDate());
        assertNull(car.getId());
    }

    @Test
    public void test_convertCarToCarModel() {
        Car car = createCar("C764375", "Ford", "Focus");
        CarModel carModel = carConverter.convertCarToCarModel(car);
        assertEquals(car.getModel(), carModel.getModel());
        assertEquals(car.getMake(), carModel.getMake());
        assertEquals(car.getIdentifier(), carModel.getIdentifier());
    }

}
