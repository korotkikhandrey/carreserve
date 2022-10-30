package com.example.carreg.service;

import com.example.carreg.converter.CarConverter;
import com.example.carreg.converter.ReservationConverter;
import com.example.carreg.domain.CarModel;
import com.example.carreg.entity.Car;
import com.example.carreg.repository.CarRepository;
import com.example.carreg.repository.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.example.carreg.utils.TestUtils.createCar;
import static com.example.carreg.utils.TestUtils.createCarModel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @SpyBean
    private CarConverter carConverter;

    @SpyBean
    private ReservationConverter reservationConverter;

    @Test
    public void test_addCar() throws Exception {
        //given
        Car car = createCar("C764375", "Ford", "Focus");
        CarModel carModel = createCarModel("C764375", "Ford", "Focus");
        when(carRepository.save(any(Car.class))).thenReturn(car);

        //when
        CarModel carModelSaved = carService.addCar(carModel);

        //then
        assertEquals(carModel.getModel(), carModelSaved.getModel());
        assertEquals(carModel.getMake(), carModelSaved.getMake());
        assertEquals(carModel.getIdentifier(), carModelSaved.getIdentifier());
        verify(carRepository).save(any(Car.class));
    }

    @Test
    public void test_updateCar_found() {

        //given
        Car car = createCar("C764375", "Ford", "Focus");
        CarModel carUpdated = createCarModel("C764376", "Ford", "Mustang");
        when(carRepository.findByIdentifier(anyString())).thenReturn(car);

        //when
        CarModel carModel = carService.updateCar("C764375", carUpdated);

        //then
        assertEquals("C764376", carModel.getIdentifier());
        assertEquals("Ford", carModel.getMake());
        assertEquals("Mustang", carModel.getModel());
    }

    @Test
    public void test_updateCar_notfound() {

        //given
        CarModel carUpdated = createCarModel("C764376", "Ford", "Focus");
        when(carRepository.findByIdentifier(anyString())).thenReturn(null);

        //when
        IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
            CarModel carModel = carService.updateCar("C764376", carUpdated);
        });

        //then
        assertNotNull(thrown);
    }

    @Test
    public void test_removeCar() {

        //given
        Car car1 = createCar("C764375", "Ford", "Focus");
        when(carRepository.findByIdentifier(anyString())).thenReturn(car1);

        //when
        carService.removeCar(car1.getIdentifier());

        //then
        verify(reservationRepository).removeReservationsByCar(any(Car.class));
        verify(carRepository).delete(any(Car.class));

    }

}
