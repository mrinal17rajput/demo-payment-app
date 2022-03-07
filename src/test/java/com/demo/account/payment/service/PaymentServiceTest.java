package com.demo.account.payment.service;

import com.demo.account.payment.entity.Account;
import com.demo.account.payment.entity.Payment;
import com.demo.account.payment.exception.InsufficientBalanceException;
import com.demo.account.payment.exception.InvalidAccountException;
import com.demo.account.payment.model.PaymentModel;
import com.demo.account.payment.model.PaymentReturnModel;
import com.demo.account.payment.repository.AccountRepository;
import com.demo.account.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() throws ParseException {
        Account Account202201 = Account.builder().accountNo(202201L).name("Cust1").balance(BigDecimal.valueOf(10000.0)).build();
        Account Account202202 = Account.builder().accountNo(202202L).name("Cust2").balance(BigDecimal.valueOf(10000.0)).build();
        Account Account202203 = Account.builder().accountNo(202203L).name("Cust3").balance(BigDecimal.valueOf(10000.0)).build();
        Account Account202204 = Account.builder().accountNo(202204L).name("Cust4").balance(BigDecimal.valueOf(10000.0)).build();
        Account Account202205 = Account.builder().accountNo(202205L).name("Cust5").balance(BigDecimal.valueOf(10000.0)).build();
        Account Account202206 = Account.builder().accountNo(202206L).name("Cust6").balance(BigDecimal.valueOf(10000.0)).build();
        List<Payment> payments = new ArrayList<>(
                Arrays.asList(
                        Payment.builder().fromAccount(Account202201).amount(BigDecimal.valueOf(100.0)).toAccount(Account202202).lastUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("27-02-2022")).build(),
                        Payment.builder().fromAccount(Account202206).amount(BigDecimal.valueOf(800.0)).toAccount(Account202202).lastUpdate(new Date()).build(),
                        Payment.builder().fromAccount(Account202201).amount(BigDecimal.valueOf(600.0)).toAccount(Account202205).lastUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("28-02-2022")).build(),
                        Payment.builder().fromAccount(Account202203).amount(BigDecimal.valueOf(500.0)).toAccount(Account202201).lastUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("02-03-2022")).build(),
                        Payment.builder().fromAccount(Account202201).amount(BigDecimal.valueOf(200.0)).toAccount(Account202203).lastUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("03-03-2022")).build(),
                        Payment.builder().fromAccount(Account202202).amount(BigDecimal.valueOf(1000.0)).toAccount(Account202204).lastUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("26-02-2022")).build()));

        Mockito.when(paymentRepository.findByAccountNo(202201L)).thenReturn(payments);
        Mockito.when(paymentRepository.findByAccountNo(202211L)).thenReturn(new ArrayList<>());

        Date startDate = new SimpleDateFormat("dd-MM-yyyy").parse("27-02-2022");
        Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse("03-03-2022");
        Mockito.when(paymentRepository.findByLastUpdateBetween(startDate, endDate)).thenReturn(payments);

        Date startDate2 = new SimpleDateFormat("dd-MM-yyyy").parse("27-02-2022");
        Date endDate2 = new SimpleDateFormat("dd-MM-yyyy").parse("28-02-2022");
        Mockito.when(paymentRepository.findByLastUpdateBetween(startDate2, endDate2)).thenReturn(new ArrayList<>());

        Account acc202201S = Account.builder().accountNo(202201L).balance(BigDecimal.valueOf(10000.0)).name("Customer1").id(1).build();
        Account acc202202S = Account.builder().accountNo(202202L).balance(BigDecimal.valueOf(10000.0)).name("Customer2").id(2).build();
        Mockito.when(accountRepository.findByAccountNo(202201L)).thenReturn(acc202201S);
        Mockito.when(accountRepository.findByAccountNo(202202L)).thenReturn(acc202202S);

    }

    @Test
    void whenAccountHasPayments_thenReturnPayments() {
        Long accountNo = 202201L;
        List<PaymentReturnModel> payments = paymentService.getLastTenPaymentsForAccount(accountNo);
        assertEquals(6, payments.size());
    }

    @Test
    void whenAccountHasNoPayments_thenReturnBlank() {
        Long accountNo = 202211L;
        List<PaymentReturnModel> payments = paymentService.getLastTenPaymentsForAccount(accountNo);
        assertEquals(new ArrayList<>(), payments);
    }

    @Test
    void whenPaymentsExistBetweenDates_thenReturnPayments() throws ParseException {
        Date startDate = new SimpleDateFormat("dd-MM-yyyy").parse("27-02-2022");
        Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse("03-03-2022");
        List<PaymentReturnModel> payments = paymentService.getPaymentsBetweenDates(startDate, endDate);
        assertEquals(6, payments.size());
    }

    @Test
    void whenNoPaymentsExistBetweenDates_thenReturnBlank() throws ParseException {
        Date startDate = new SimpleDateFormat("dd-MM-yyyy").parse("27-02-2022");
        Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse("28-02-2022");
        List<PaymentReturnModel> payments = paymentService.getPaymentsBetweenDates(startDate, endDate);
        assertEquals(0, payments.size());
    }

    @Test
    void whenSufficientBalance_thenTransferThePayment() {
        PaymentModel paymentModel = PaymentModel.builder()
                .fromAccountNo(202201L)
                .toAccountNo(202202L)
                .amount(BigDecimal.valueOf(200.0))
                .build();
        ResponseEntity<Object> responseEntity = paymentService.transferPayment(paymentModel);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("new Payment created with the reference number", Objects.requireNonNull(responseEntity.getBody()).toString().split(":")[0]);
    }

    @Test
    void whenInsufficientBalance_thenThrowInsufficientBalanceException() {
        PaymentModel paymentModel = PaymentModel.builder()
                .fromAccountNo(202201L)
                .toAccountNo(202202L)
                .amount(BigDecimal.valueOf(20000.0))
                .build();
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> paymentService.transferPayment(paymentModel));
        assertEquals("insufficient funds", exception.getMessage());
    }

    @Test
    void whenInvalidToAccount_thenThrowInvalidAccountException() {
        PaymentModel paymentModel = PaymentModel.builder()
                .fromAccountNo(202204L)
                .toAccountNo(202203L)
                .amount(BigDecimal.valueOf(2000.0))
                .build();
        Account acc202204 = Account.builder().accountNo(202204L).balance(BigDecimal.valueOf(10000.0)).name("Customer4").id(2).build();
        Mockito.when(accountRepository.findByAccountNo(202204L)).thenReturn(acc202204);
        Mockito.when(accountRepository.findByAccountNo(202203L)).thenReturn(null);
        InvalidAccountException exception = assertThrows(InvalidAccountException.class, () ->paymentService.transferPayment(paymentModel));
        assertEquals("invalid to account", exception.getMessage());
    }

    @Test
    void whenInvalidFromAccount_thenThrowInvalidAccountException() {
        PaymentModel paymentModel = PaymentModel.builder()
                .fromAccountNo(202204L)
                .toAccountNo(202203L)
                .amount(BigDecimal.valueOf(20000.0))
                .build();
        Mockito.when(accountRepository.findByAccountNo(202204L)).thenReturn(null);
        InvalidAccountException exception = assertThrows(InvalidAccountException.class, () ->paymentService.transferPayment(paymentModel));
        assertEquals("invalid from account", exception.getMessage());
    }
}