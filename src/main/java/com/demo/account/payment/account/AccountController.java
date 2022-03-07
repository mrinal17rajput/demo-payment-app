package com.demo.account.payment.account;

import com.demo.account.payment.entity.Account;
import com.demo.account.payment.repository.AccountRepository;
import com.demo.account.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/createAccount")
    public Account createAccount(@RequestBody AccountModel accountModel) {
        Account account = Account.builder()
                .accountNo(accountModel.getAccountNo())
                .balance(accountModel.getBalance())
                .name(accountModel.getName())
                .build();
        return accountRepository.save(account);
    }

    @GetMapping("/findAll")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @DeleteMapping("/deleteAllPayments")
    public void deletePayments() {
        paymentRepository.deleteAll();
    }
}
