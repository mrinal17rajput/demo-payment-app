package com.demo.account.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ExceptionResponse {
    private Date timeStamp;
    private String message;
    private String details;
}
