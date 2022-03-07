package com.demo.account.payment.model;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@Data
public class PaymentReturnModel {
    private Long fromAccountNo;
    private Long toAccountNo;
    private BigDecimal amount;
    private Date lastUpdated;
}
