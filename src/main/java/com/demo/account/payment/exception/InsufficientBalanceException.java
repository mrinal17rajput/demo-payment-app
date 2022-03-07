package com.demo.account.payment.exception;

public class InsufficientBalanceException extends RuntimeException{

    public InsufficientBalanceException(String msg) {
        super(msg);
    }
}
