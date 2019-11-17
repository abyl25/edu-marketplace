package com.seniorproject.educationplatform.controllers;

import com.paypal.base.rest.PayPalRESTException;
import com.seniorproject.educationplatform.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentController {
    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment/create")
    public Map<String, Object> createPayment(@RequestParam("total") String total) throws PayPalRESTException {
        System.out.println("LOG: createPayment(): total: " + total);
        return paymentService.createPayment(total);
    }

    @PostMapping("/payment/execute")
    public Map<String, Object> executePayment(@RequestParam("paymentId") String paymentId, @RequestParam("payerId") String payerId) throws PayPalRESTException {
        System.out.println("LOG: executePayment(): paymentId: " + paymentId + "; payerId: " + payerId);
        return paymentService.executePayment(paymentId, payerId);
    }

}
