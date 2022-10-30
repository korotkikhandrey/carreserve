package com.example.carreg.controller;

import com.example.carreg.domain.CarModel;
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
     * @param carModel
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
    public ResponseEntity<Void> addCar(@Valid @RequestBody CarModel carModel) {
        ResponseEntity responseEntity;
        CarModel result = carService.addCar(carModel);
        responseEntity = ResponseEntity.ok().body(result);
        return responseEntity;
    }


    @PutMapping(value ="/car",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car was updated or not updated due to object was not found to be updated. "),
            @ApiResponse(code = 400, message = "Probably car object is not valid. All the fields should be nonnull, id should match C<number> format.")} )
    @ResponseBody
    public ResponseEntity<CarModel> updateCar(@RequestParam String identifier,
                                              @Valid @RequestBody CarModel carModel) {
        CarModel updateCar;
        ResponseEntity responseEntity;
        try {
            updateCar = carService.updateCar(identifier, carModel);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(updateCar);
        } catch (Exception exception) {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }

        return responseEntity;
    }

    /**
     * Removes car by identifier.
     * @param identifier
     * @return ResponseEntity with appropriate message.
     */
    @DeleteMapping(value ="/car/{identifier}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car was removed."),
            @ApiResponse(code = 400, message = "Probably car object is not valid. All the fields should be nonnull, identifier should match C<number> format.")} )
    @ResponseBody
    public ResponseEntity<Void> removeCar(@PathVariable String identifier) {

        ResponseEntity responseEntity;
        String message;
        try {
            message = carService.removeCar(identifier);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception exception) {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }

        return responseEntity;
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
    public ResponseEntity<List<CarModel>> getAllCars() {
        return new ResponseEntity<>(carService.getAllCars(), HttpStatus.OK);
    }
}
