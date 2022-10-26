package com.example.carreg.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * Car entity.
 */
@Data
@AllArgsConstructor
@Entity
@Table(name = "car")
@NoArgsConstructor
public class Car extends AbstractEntity {

    @Pattern(regexp = "^C[0-9]*$", flags = { Pattern.Flag.CASE_INSENSITIVE }, message = "The car id is invalid. Should match C<number> format!")
    @NotEmpty(message = "id must not be empty")
    @NotNull
    @Column(name="licensePlate")
    private String licensePlate;
    @NotEmpty(message = "make must not be empty")
    @NotNull
    @Column(name="make")
    private String make;
    @NotEmpty(message = "model must not be empty")
    @NotNull
    @Column(name="model")
    private String model;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(licensePlate, car.licensePlate) && Objects.equals(make, car.make) && Objects.equals(model, car.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licensePlate, make, model);
    }
}
