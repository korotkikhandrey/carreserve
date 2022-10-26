package com.example.carreg.service;

import com.example.carreg.entity.Car;
import com.example.carreg.repository.CarRepository;
import com.example.carreg.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.example.carreg.utils.TestUtils.createCar;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        Car carUpdated = createCar("C764376", "Ford", "Focus");
        when(carRepository.findByMakeAndModel(anyString(), anyString())).thenReturn(car);

        //when
        carService.updateCar(carUpdated);

        //then
        verify(carRepository).delete(any(Car.class));
        verify(carRepository).save(any(Car.class));
    }

    @Test
    public void test_updateCar_notfound() {

        //given
        Car car = createCar("C764375", "Ford", "Focus");
        Car carUpdated = createCar("C764376", "Ford", "Focus");
        when(carRepository.findByMakeAndModel(anyString(), anyString())).thenReturn(null);

        //when
        carService.updateCar(carUpdated);

        //then
        verify(carRepository, never()).delete(any(Car.class));
        verify(carRepository).save(any(Car.class));
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
