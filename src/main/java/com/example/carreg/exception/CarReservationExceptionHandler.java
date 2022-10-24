package com.example.carreg.exception;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
@AllArgsConstructor
@Slf4j
public class CarReservationExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DELIMITER = ", ";

    private static final String VALUE_IS_INCORRECT_TEMPLATE = "%1$s contains incorrect value \"%2$s\", reason: %3$s";

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
            return new ResponseEntity(String.join(";\n", invalidAttributes), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.accepted().contentLength(0).build();
    }


    @ExceptionHandler({IllegalStateException.class})
    public final ResponseEntity handleImportBusinessException(IllegalStateException ex, WebRequest request) {

        return ResponseEntity.accepted().contentLength(0).build();
    }

}
