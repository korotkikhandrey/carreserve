package com.example.carreg.data;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Data
public class Car {

    @Pattern(regexp = "^C[0-9]*$", flags = { Pattern.Flag.CASE_INSENSITIVE }, message = "The car id is invalid. Should match C<number> format!")
    @NotEmpty(message = "id must not be empty")
    private String id;
    @NotEmpty(message = "make must not be empty")
    private String make;
    @NotEmpty(message = "model must not be empty")
    private String model;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id) && Objects.equals(make, car.make) && Objects.equals(model, car.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, make, model);
    }
}
