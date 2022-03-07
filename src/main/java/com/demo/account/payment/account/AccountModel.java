package com.demo.account.payment.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel {
    private Long accountNo;
    private String name;
    private BigDecimal balance;
}
