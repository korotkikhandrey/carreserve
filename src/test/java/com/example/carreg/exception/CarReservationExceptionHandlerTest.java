package com.example.carreg.exception;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link CarReservationExceptionHandler}
 */
public class CarReservationExceptionHandlerTest {

    private CarReservationExceptionHandler carReservationExceptionHandler = new CarReservationExceptionHandler();

    /**
     * Test for Bad request response exception handler.
     */
    @Test
    public void test_handleMethodArgumentNotValid_400() {

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                mock(MethodParameter.class),
                mock(BindingResult.class));

        String objectName = "SomeObject";
        String field = "someField";
        String defaultMessage = "Incorrect value!!!";
        FieldError fieldError = new FieldError(objectName, field, defaultMessage);

        List<FieldError> fieldErrors = Arrays.asList(fieldError);
        when(ex.getBindingResult().getFieldErrors()).thenReturn(fieldErrors);

        //when
        ResponseEntity<Object> responseEntity = carReservationExceptionHandler
                .handleMethodArgumentNotValid(
                        ex,
                        HttpHeaders.EMPTY,
                        HttpStatus.BAD_REQUEST,
                        mock(WebRequest.class));

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Test for ACCEPTED response exception handler.
     */
    @Test
    public void test_handleMethodArgumentNotValid_202() {

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                mock(MethodParameter.class),
                mock(BindingResult.class));

        List<FieldError> fieldErrors = Collections.EMPTY_LIST;
        when(ex.getBindingResult().getFieldErrors()).thenReturn(fieldErrors);

        //when
        ResponseEntity<Object> responseEntity = carReservationExceptionHandler
                .handleMethodArgumentNotValid(
                        ex,
                        HttpHeaders.EMPTY,
                        HttpStatus.ACCEPTED,
                        mock(WebRequest.class));

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }
}
