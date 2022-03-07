package com.demo.account.payment.controller;

import com.demo.account.payment.exception.InsufficientBalanceException;
import com.demo.account.payment.model.PaymentModel;
import com.demo.account.payment.model.PaymentReturnModel;
import com.demo.account.payment.service.PaymentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    List<PaymentReturnModel> payments;

    @BeforeEach
    void setUp() {
        payments = new ArrayList<>(
                Arrays.asList(new PaymentReturnModel(202201L, 202202L, new BigDecimal("200.0"), new Date()),
                        new PaymentReturnModel(202203L, 202201L, new BigDecimal("200.0"), new Date()),
                        new PaymentReturnModel(202201L, 202206L, new BigDecimal("200.0"), new Date()),
                        new PaymentReturnModel(202201L, 202208L, new BigDecimal("200.0"), new Date()),
                        new PaymentReturnModel(202209L, 202201L, new BigDecimal("200.0"), new Date()),
                        new PaymentReturnModel(202201L, 202204L, new BigDecimal("200.0"), new Date()),
                        new PaymentReturnModel(202201L, 202202L, new BigDecimal("200.0"), new Date()),
                        new PaymentReturnModel(202201L, 202204L, new BigDecimal("200.0"), new Date()),
                        new PaymentReturnModel(202204L, 202201L, new BigDecimal("200.0"), new Date())));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void validTransferPaymentTest() throws Exception {
        PaymentModel paymentModel = PaymentModel.builder().fromAccountNo(202201L).toAccountNo(202202L).amount(new BigDecimal("250.0")).build();

        Mockito.when(paymentService.transferPayment(paymentModel)).
                thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("new Payment created with the reference number: " + UUID.randomUUID().toString()));

        mockMvc.perform(MockMvcRequestBuilders.post("/payment/transferPayment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\t\"fromAccountNo\": 202201,\n" +
                                "\t\"toAccountNo\": 202202,\n" +
                                "\t\"amount\":250.0\n" +
                                "}"
                        ))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void invalidPaymentTransferTest() throws Exception {
        PaymentModel paymentModel = PaymentModel.builder().fromAccountNo(202201L).toAccountNo(202202L).amount(new BigDecimal("250.0")).build();

        Mockito.when(paymentService.transferPayment(paymentModel)).thenThrow(InsufficientBalanceException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/payment/transferPayment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\t\"fromAccountNo\": 202201,\n" +
                                "\t\"toAccountNo\": 202202,\n" +
                                "\t\"amount\":250.0\n" +
                                "}"
                        ))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void getLastTenPaymentsTest() throws Exception {
        Mockito.when(paymentService.getLastTenPaymentsForAccount(202201L)).thenReturn(payments);
        mockMvc.perform(MockMvcRequestBuilders.get("/payment/getLastTenPayments/202201")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getNullLastTenPaymentsTest() throws Exception {
        Mockito.when(paymentService.getLastTenPaymentsForAccount(202201L)).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/payment/getLastTenPayments/202201")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(new ArrayList<>()));
    }

    @Test
    public void getPaymentsBetweenDatesTest() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sDate = sdf.parse("2022-02-27");
        Date eDate = sdf.parse("2022-03-03");
        Mockito.when(paymentService.getPaymentsBetweenDates(sDate, eDate)).thenReturn(payments);
        mockMvc.perform(MockMvcRequestBuilders.get("/payment/getPaymentsBetweenDates/2022-02-27/2022-03-03")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getNullPaymentsBetweenDatesTest() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sDate = sdf.parse("2022-02-27");
        Date eDate = sdf.parse("2022-03-03");
        Mockito.when(paymentService.getPaymentsBetweenDates(sDate, eDate)).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/payment/getPaymentsBetweenDates/2022-02-27/2022-03-03")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(new ArrayList<>()));
    }
}