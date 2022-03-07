package com.demo.account.payment.exception;

public class InvalidAccountException extends RuntimeException{
    public InvalidAccountException(String msg) {
        super(msg);
    }
}
