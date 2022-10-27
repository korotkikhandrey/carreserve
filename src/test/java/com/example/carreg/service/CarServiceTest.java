package com.example.carreg.service;

import com.example.carreg.entity.Car;
import com.example.carreg.entity.Reservation;
import com.example.carreg.repository.CarRepository;
import com.example.carreg.repository.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.example.carreg.utils.TestUtils.createCar;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link CarService}
 */
@ExtendWith(SpringExtension.class)
public class CarServiceTest {

    @MockBean
    private CarRepository carRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @SpyBean
    private CarService carService;

    @Test
    public void test_addCar() {
        //given
        Car car = createCar("C764375", "Ford", "Focus");

        //when
        carService.addCar(car);

        //then
        verify(carRepository).save(any(Car.class));
    }

    @Test
    public void test_updateCar_found() {

        //given
        Car car = createCar("C764375", "Ford", "Focus");
        Car carUpdated = createCar("C764376", "Ford", "Mustang");
        when(carRepository.findByLicensePlate(anyString())).thenReturn(car);

        //when
        String message = carService.updateCar("C764375", carUpdated);

        //then
        /*assertEquals("C764376", foundCar.getLicensePlate());
        assertEquals("Ford", foundCar.getMake());
        assertEquals("Mustang", foundCar.getModel());*/
    }

    @Test
    public void test_updateCar_notfound() {

        //given
        Car carUpdated = createCar("C764376", "Ford", "Focus");
        when(carRepository.findByLicensePlate(anyString())).thenReturn(null);

        //when
        String message = carService.updateCar("C764376", carUpdated);


        //then
        assertTrue(message.equals("Car with license plate [C764376] not found to be updated."));
    }

    @Test
    public void test_removeCar() {

        //given
        Car car1 = createCar("C764375", "Ford", "Focus");
        when(carRepository.findByLicensePlate(anyString())).thenReturn(car1);

        //when
        carService.removeCar(car1.getLicensePlate());

        //then
        verify(reservationRepository).removeReservationsByCar(any(Car.class));
        verify(carRepository).delete(any(Car.class));

    }

}
