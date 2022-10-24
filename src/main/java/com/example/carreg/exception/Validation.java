package com.example.carreg.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for error messages list.
 */
public class Validation {

    private List<String> errors = new ArrayList<>();

    public List<String> getErrors() {
        return errors;
    }
}
