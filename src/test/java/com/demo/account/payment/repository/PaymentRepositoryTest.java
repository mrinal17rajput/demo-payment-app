package com.demo.account.payment.repository;

import com.demo.account.payment.entity.Account;
import com.demo.account.payment.entity.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() throws ParseException {
        Account Account202201 = Account.builder().accountNo(202201L).name("Cust1").balance(BigDecimal.valueOf(10000.0)).build();
        Account Account202202 = Account.builder().accountNo(202202L).name("Cust2").balance(BigDecimal.valueOf(10000.0)).build();
        Account Account202203 = Account.builder().accountNo(202203L).name("Cust3").balance(BigDecimal.valueOf(10000.0)).build();
        Account Account202204 = Account.builder().accountNo(202204L).name("Cust4").balance(BigDecimal.valueOf(10000.0)).build();
        Account Account202205 = Account.builder().accountNo(202205L).name("Cust5").balance(BigDecimal.valueOf(10000.0)).build();
        Account Account202206 = Account.builder().accountNo(202206L).name("Cust6").balance(BigDecimal.valueOf(10000.0)).build();
        List<Payment> paymentList = new ArrayList<>(
                Arrays.asList(
                Payment.builder().fromAccount(Account202201).amount(BigDecimal.valueOf(100.0)).toAccount(Account202202).lastUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("27-02-2022")).build(),
                Payment.builder().fromAccount(Account202206).amount(BigDecimal.valueOf(800.0)).toAccount(Account202202).lastUpdate(new Date()).build(),
                Payment.builder().fromAccount(Account202201).amount(BigDecimal.valueOf(600.0)).toAccount(Account202205).lastUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("28-02-2022")).build(),
                Payment.builder().fromAccount(Account202203).amount(BigDecimal.valueOf(500.0)).toAccount(Account202201).lastUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("02-03-2022")).build(),
                Payment.builder().fromAccount(Account202201).amount(BigDecimal.valueOf(200.0)).toAccount(Account202203).lastUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("03-03-2022")).build(),
                Payment.builder().fromAccount(Account202202).amount(BigDecimal.valueOf(1000.0)).toAccount(Account202204).lastUpdate(new SimpleDateFormat("dd-MM-yyyy").parse("26-02-2022")).build()));
        paymentList.forEach(payment -> {
            testEntityManager.persist(payment);
        });

    }

    @Test
    public void whenPaymentsExistForAccount_thenReturnPayments() {
        List<Payment> payments = paymentRepository.findByAccountNo(202201L);
        double[] amounts = new double[]{200.0, 500.0, 600.0, 100.0};
        assertEquals(4, payments.size());
        for(int i = 0; i < payments.size(); i++) {
            assertEquals( BigDecimal.valueOf(amounts[i]), payments.get(i).getAmount());
        }
    }

    @Test
    public void whenNoPaymentsExistForAccount_thenReturnEmptyList() {
        List<Payment> payments = paymentRepository.findByAccountNo(202207L);
        assertEquals(new ArrayList<>(), payments);
    }

    @Test
    public void whenPaymentsExistBetweenDates_thenReturnPayments() throws ParseException {
        List<Payment> payments = paymentRepository.findByLastUpdateBetween(new SimpleDateFormat("dd-MM-yyyy").parse("27-02-2022"),
                new SimpleDateFormat("dd-MM-yyyy").parse("02-03-2022"));
        double[] amounts = new double[]{100.0, 600.0, 500.0};
        assertEquals(3, payments.size());
        for(int i = 0; i < payments.size(); i++) {
            assertEquals( BigDecimal.valueOf(amounts[i]), payments.get(i).getAmount());
        }
    }

    @Test
    public void whenNoPaymentsExistBetweenDates_thenReturnEmptyList() throws ParseException {
        List<Payment> payments = paymentRepository.findByLastUpdateBetween(new SimpleDateFormat("dd-MM-yyyy").parse("24-02-2022"),
                new SimpleDateFormat("dd-MM-yyyy").parse("25-02-2022"));
        assertEquals(new ArrayList<>(), payments);
    }
}