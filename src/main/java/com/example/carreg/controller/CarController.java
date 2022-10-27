package com.example.carreg.controller;

import com.example.carreg.entity.Car;
import com.example.carreg.service.CarService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Car restcontroller.
 */
@RestController
@RequestMapping("/registration")
@AllArgsConstructor
public class CarController {

    private final CarService carService;

    /**
     * Adds car endpoint.
     * @param car
     * @return ResponseEntity with {@link Car}
     */
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
    public ResponseEntity<Object> updateCar(@RequestParam String plateLicense,
                                            @Valid @RequestBody Car car) {
        carService.updateCar(plateLicense, car);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    /**
     * Removes car by libecnse plate endpoint.
     * @param licensePlate
     * @return ResponseEntity with appropriate message.
     */
    @DeleteMapping(value ="/car/{licensePlate}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car was removed."),
            @ApiResponse(code = 400, message = "Probably car object is not valid. All the fields should be nonnull, licensePlate should match C<number> format.")} )
    @ResponseBody
    public ResponseEntity<String> removeCar(@PathVariable String licensePlate) {
        String response = carService.removeCar(licensePlate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * All cars in a system endpoint.
     * @return ResponseEntity with cars list.
     */
    @GetMapping(value ="/car",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All the cars in the system."),
            @ApiResponse(code = 400, message = "Probably car object is not valid. All the fields should be nonnull, id should match C<number> format.")} )
    @ResponseBody
    public ResponseEntity<List<Car>> getAllCars() {
        return new ResponseEntity<>(carService.getAllCars(), HttpStatus.OK);
    }
}
