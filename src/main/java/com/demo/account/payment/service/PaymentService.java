package com.demo.account.payment.service;

import com.demo.account.payment.entity.Account;
import com.demo.account.payment.entity.Payment;
import com.demo.account.payment.exception.InsufficientBalanceException;
import com.demo.account.payment.exception.InvalidAccountException;
import com.demo.account.payment.model.PaymentModel;
import com.demo.account.payment.model.PaymentReturnModel;
import com.demo.account.payment.repository.AccountRepository;
import com.demo.account.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<PaymentReturnModel> getLastTenPaymentsForAccount(Long accountNo) {
        List<Payment> payments = paymentRepository.findByAccountNo(accountNo);
        return convertPaymentToPaymentReturnModel(payments);
    }

    public List<PaymentReturnModel> getPaymentsBetweenDates(Date startDate, Date endDate) {
        List<Payment> payments = paymentRepository.findByLastUpdateBetween(startDate, endDate);
        return convertPaymentToPaymentReturnModel(payments);
    }

    @Transactional
    public ResponseEntity<Object> transferPayment(PaymentModel paymentModel) {
        Optional<Account> fromAccountOptional = Optional.ofNullable(accountRepository.findByAccountNo(paymentModel.getFromAccountNo()));

        fromAccountOptional.orElseThrow(() -> new InvalidAccountException("invalid from account"));

        Account fromAccount = fromAccountOptional.get();
        if(fromAccount.getBalance().compareTo(paymentModel.getAmount()) < 0) {
            throw new InsufficientBalanceException("insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(paymentModel.getAmount()));
        accountRepository.save(fromAccount);

        Optional<Account> toAccountOptional = Optional.ofNullable(accountRepository.findByAccountNo(paymentModel.getToAccountNo()));

        toAccountOptional.orElseThrow(() -> new InvalidAccountException("invalid to account"));

        Account toAccount = toAccountOptional.get();

        toAccount.setBalance(toAccount.getBalance().add(paymentModel.getAmount()));
        accountRepository.save(toAccount);

        Payment payment = Payment.builder()
                .fromAccount(fromAccount)
                .amount(paymentModel.getAmount())
                .toAccount(toAccount)
                .lastUpdate(new Date())
                .referenceNo(UUID.randomUUID().toString())
                .build();
        paymentRepository.save(payment);

        return ResponseEntity.status(HttpStatus.CREATED).body("new Payment created with the reference number: " + payment.getReferenceNo());
    }

    private List<PaymentReturnModel> convertPaymentToPaymentReturnModel(List<Payment> payments) {
        return payments.stream().map(payment -> {return new PaymentReturnModel(
                payment.getFromAccount().getAccountNo(), payment.getToAccount().getAccountNo(),
                payment.getAmount(), payment.getLastUpdate());}).collect(Collectors.toList());
    }
}
