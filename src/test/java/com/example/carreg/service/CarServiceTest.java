package com.example.carreg.service;

import com.example.carreg.data.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.example.carreg.utils.TestUtils.createCar;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class CarServiceTest {

    @SpyBean
    private CarService carService;

    @BeforeEach
    public void setup() {
        carService.getAllCars().clear();
    }

    @Test
    public void test_addCar() {
        //given
        Car car = createCar("C764375", "Ford", "Focus");

        //when
        carService.addCar(car);

        //then
        assertTrue(carService.getAllCars().contains(car));
    }

    @Test
    public void test_updateCar() {

        //given
        Car car = createCar("C764375", "Ford", "Focus");
        Car carUpdated = createCar("C764376", "Ford", "Focus");
        Car carNotFoundToAdd = createCar("C764377", "Ford", "Fiesta");

        //when
        carService.addCar(car);
        carService.updateCar(carUpdated);
        carService.updateCar(carNotFoundToAdd);

        //then
        assertTrue(carService.getAllCars().contains(carUpdated));
        assertFalse(carService.getAllCars().contains(car));
        assertTrue(carService.getAllCars().contains(carNotFoundToAdd));
    }

    @Test
    public void test_removeCar() {

        //given
        Car car1 = createCar("C764375", "Ford", "Focus");
        Car car2 = createCar("C764377", "Ford", "Fiesta");
        carService.addCar(car1);
        carService.addCar(car2);
        assertTrue(carService.getAllCars().contains(car1));
        assertTrue(carService.getAllCars().contains(car2));

        //when
        carService.removeCar(car2.getId());

        //then
        assertTrue(carService.getAllCars().contains(car1));
        assertFalse(carService.getAllCars().contains(car2));
    }

}
