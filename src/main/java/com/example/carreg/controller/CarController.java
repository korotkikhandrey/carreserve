package com.example.carreg.controller;

import com.example.carreg.data.Car;
import com.example.carreg.service.CarService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/registration")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping(value ="/car",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car was added to the system."),
            @ApiResponse(code = 400, message = "Probably car object is not valid. All the fields should be nonnull, id should match C<number> format.")} )
    @ResponseBody
    public ResponseEntity<Car> addCar(@Valid @RequestBody Car car) {
        carService.addCar(car);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @PutMapping(value ="/car",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car was updated."),
            @ApiResponse(code = 400, message = "Probably car object is not valid. All the fields should be nonnull, id should match C<number> format.")} )
    @ResponseBody
    public ResponseEntity<Car> updateCar(@Valid @RequestBody Car car) {
        carService.updateCar(car);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @DeleteMapping(value ="/car/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car was removed."),
            @ApiResponse(code = 400, message = "Probably car object is not valid. All the fields should be nonnull, id should match C<number> format.")} )
    @ResponseBody
    public ResponseEntity<String> removeCar(@PathVariable String id) {
        String response = carService.removeCar(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value ="/car",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All the cars in the system."),
            @ApiResponse(code = 400, message = "Probably car object is not valid. All the fields should be nonnull, id should match C<number> format.")} )
    @ResponseBody
    public ResponseEntity<Set<Car>> getAllCars() {
        return new ResponseEntity<>(carService.getAllCars(), HttpStatus.OK);
    }
}
