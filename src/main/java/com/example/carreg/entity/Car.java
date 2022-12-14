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

/**
 * Car entity.
 */
@Data
@Entity
@Table(name = "car")
@NoArgsConstructor
@AllArgsConstructor
public class Car extends AbstractEntity {

    @Pattern(regexp = "^C[0-9]+$", flags = { Pattern.Flag.CASE_INSENSITIVE }, message = "The car id is invalid. Should match C<number> format!")
    @NotEmpty(message = "id must not be empty")
    @NotNull
    @Column(name="identifier")
    private String identifier;
    @NotEmpty(message = "make must not be empty")
    @NotNull
    @Column(name="make")
    private String make;
    @NotEmpty(message = "model must not be empty")
    @NotNull
    @Column(name="model")
    private String model;


}
