package com.demo.account.payment.util;

import com.demo.account.payment.entity.Account;
import com.demo.account.payment.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements ApplicationRunner {

    private final AccountRepository accountRepository;

    @Autowired
    public DataLoader(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void run(ApplicationArguments args) {
        accountRepository.save(Account.builder().accountNo(202201L).balance(BigDecimal.valueOf(10000)).name("Customer1").build());
        accountRepository.save(Account.builder().accountNo(202202L).balance(BigDecimal.valueOf(20000.0)).name("Customer2").build());
        accountRepository.save(Account.builder().accountNo(202203L).balance(BigDecimal.valueOf(30000.0)).name("Customer3").build());
        accountRepository.save(Account.builder().accountNo(202204L).balance(BigDecimal.valueOf(40000.0)).name("Customer4").build());
    }
}
