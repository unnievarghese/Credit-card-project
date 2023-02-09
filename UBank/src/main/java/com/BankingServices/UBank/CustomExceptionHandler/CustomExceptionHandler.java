package com.BankingServices.UBank.CustomExceptionHandler;

import com.BankingServices.UBank.Util.Constants;
import com.BankingServices.UBank.Util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), Constants.FAILED);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
