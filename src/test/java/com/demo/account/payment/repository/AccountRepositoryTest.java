package com.demo.account.payment.repository;

import com.demo.account.payment.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        Account account = Account.builder()
                .accountNo(202201L)
                .balance(BigDecimal.valueOf(10000.0))
                .name("Customer1")
                .build();
        testEntityManager.persist(account);
    }

    @Test
    public void whenFindByAccountNo_thenReturnAccount() {
        Account account = accountRepository.findByAccountNo(202201L);
        assertEquals(BigDecimal.valueOf(10000.0), account.getBalance());
        assertEquals("Customer1", account.getName());
    }

    @Test
    public void whenFindByAccountNoDoesNotExist_thenReturnNull() {
        Account account = accountRepository.findByAccountNo(202202L);
        assertNull(account);
    }
}