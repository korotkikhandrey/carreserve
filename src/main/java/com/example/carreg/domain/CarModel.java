package com.example.carreg.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * Domain model for Car.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarModel {

    @Pattern(regexp = "^C[0-9]+$", flags = { Pattern.Flag.CASE_INSENSITIVE }, message = "The car id is invalid. Should match C<number> format!")
    private String identifier;
    @NotEmpty(message = "make must not be empty")
    @NotNull
    private String make;
    @NotEmpty(message = "model must not be empty")
    @NotNull
    private String model;

}
