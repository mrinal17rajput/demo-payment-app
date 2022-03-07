package com.demo.account.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModel {
    @Digits(integer = 6, fraction = 0)
    private Long fromAccountNo;
    @Digits(integer = 6, fraction = 0)
    private Long toAccountNo;
    @DecimalMin("0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal amount;
}
