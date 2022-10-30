package com.example.carreg.exception;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.carreg.exception.Messages.UNIQUE_CONSTRAINT_VIOLATION;

/**
 * Handling validation for incoming JSON data.
 */
@ControllerAdvice
@RestController
@AllArgsConstructor
@Slf4j
public class CarReservationExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DELIMITER = ", ";

    private static final String VALUE_IS_INCORRECT_TEMPLATE = "%1$s contains incorrect value \"%2$s\", reason: %3$s";

    /**
     * Gets error message in case of non-valid value for JSON object fields.
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Collection<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        Function.identity(),
                        (f, s) -> new FieldError(
                                f.getObjectName(),
                                f.getField(),
                                f.getRejectedValue(),
                                f.isBindingFailure(),
                                null,
                                null,
                                String.join(DELIMITER, f.getDefaultMessage(), s.getDefaultMessage()))))
                .values();

        List<String> invalidAttributes = fieldErrors.stream()
                .map(v -> String.format(VALUE_IS_INCORRECT_TEMPLATE,
                        v.getField(), v.getRejectedValue(), v.getDefaultMessage()))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(invalidAttributes)) {
            return new ResponseEntity(String.join(";\\n", invalidAttributes), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.accepted().contentLength(0).build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity handleConstraintViolationException(DataIntegrityViolationException ex, WebRequest request) {
        log.error("The following exception registered in the exception resolver: [{}]", ex);
        return ResponseEntity.internalServerError().body(UNIQUE_CONSTRAINT_VIOLATION);
    }
}
