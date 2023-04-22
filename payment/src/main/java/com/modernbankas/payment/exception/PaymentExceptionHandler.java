package com.modernbankas.payment.exception;

import com.modernbankas.payment.service.impl.AccountServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Logger;

/**
 * Exception handler class handle all the exception happens in the flow
 * defined specific handler for each type exception and global handler
 * add more handlers more specific handling
 */
@ControllerAdvice
public class PaymentExceptionHandler {
    Logger logger = Logger.getLogger(PaymentExceptionHandler.class.getName());
    //Handle paymentExceptions
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<String> paymentExceptionHandler(PaymentException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> globalExceptionHandler(Exception ex) {
        logger.info("exception reason"+ex.getMessage());
        return new ResponseEntity("Some Technical issues try later", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
