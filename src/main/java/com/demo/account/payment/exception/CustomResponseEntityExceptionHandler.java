package com.demo.account.payment.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@ResponseStatus
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    public final ResponseEntity<ExceptionResponse> handleInsufficientBalanceException(InsufficientBalanceException exception,
                                                                                      WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                exception.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }

    @ExceptionHandler(InvalidAccountException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidAccountException(InvalidAccountException exception,
                                                                                 WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                exception.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "validation failed", ex.getBindingResult().toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}