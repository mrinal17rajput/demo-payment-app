package com.demo.account.payment.controller;

import com.demo.account.payment.model.PaymentModel;
import com.demo.account.payment.model.PaymentReturnModel;
import com.demo.account.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/getLastTenPayments/{accountNo}")
    public List<PaymentReturnModel> getLastTenPayments(@PathVariable Long accountNo) {
        return paymentService.getLastTenPaymentsForAccount(accountNo);
    }

    @GetMapping("/getPaymentsBetweenDates/{startDate}/{endDate}")
    public List<PaymentReturnModel> getPaymentsBetweenDates(@PathVariable String startDate, @PathVariable String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sDate = sdf.parse(startDate);
        Date eDate = sdf.parse(endDate);
        return paymentService.getPaymentsBetweenDates(sDate, eDate);
    }

    @PostMapping("/transferPayment")
    public ResponseEntity<Object> transferPayment(@Valid @RequestBody PaymentModel paymentModel) {
        return paymentService.transferPayment(paymentModel);
    }
}
