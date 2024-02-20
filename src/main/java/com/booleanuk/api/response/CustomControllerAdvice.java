package com.booleanuk.api.response;

import com.booleanuk.api.exceptions.CustomDataNotFoundException;
import com.booleanuk.api.exceptions.CustomParamaterConstraintException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomControllerAdvice {


    @ExceptionHandler(CustomDataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomDataNotFoundExceptions(Exception e) {
        HttpStatus status = HttpStatus.NOT_FOUND; // 404

        Map<String, String> messages = new HashMap<>();
        messages.put("message", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(status.name(), messages), status);

    }


    @ExceptionHandler(CustomParamaterConstraintException.class)
    public ResponseEntity<ErrorResponse> handleCustomParameterConstraintExceptions(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // 400

        Map<String, String> messages = new HashMap<>();
        messages.put("message", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(status.name(), messages), status);
    }


}
