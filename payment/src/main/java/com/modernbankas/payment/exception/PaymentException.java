package com.modernbankas.payment.exception;

/**
 * Custom exception class for payment Exception
 * add more exceptions here if needed
 */
public class PaymentException extends RuntimeException{
    public PaymentException(String message) {
        super(message);
    }
}
