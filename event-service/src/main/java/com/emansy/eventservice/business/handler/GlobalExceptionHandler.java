package com.emansy.eventservice.business.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorModel> handlerResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorModel errorModel = ErrorModel.builder().message(ex.getLocalizedMessage()).success(true).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<ErrorModel>(errorModel, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorModel> handlerHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorModel errorModel = ErrorModel.builder().message(ex.getMessage()).success(true).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<ErrorModel>(errorModel, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handlerHttpMessageNotReadableException(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorModel> handlerConstraintViolationException(ConstraintViolationException ex) {
        ErrorModel errorModel = ErrorModel.builder().message(ex.getMessage()).success(true).status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorModel);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<ErrorModel> handlerNoContentException(NoContentException ex) {
        ErrorModel errorModel = ErrorModel.builder().message(ex.getLocalizedMessage()).success(true).status(HttpStatus.NO_CONTENT).build();
        return new ResponseEntity<ErrorModel>(errorModel, HttpStatus.NO_CONTENT);
    }
}
