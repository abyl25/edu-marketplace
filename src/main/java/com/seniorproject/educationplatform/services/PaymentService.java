package com.seniorproject.educationplatform.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    private APIContext apiContext;

    @Autowired
    public PaymentService(APIContext apiContext) {
        this.apiContext = apiContext;
    }

    public Map<String, Object> createPayment(String total) throws PayPalRESTException {
        Map<String, Object> response = new HashMap<>();

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(total);

        Transaction transaction = new Transaction();
//        transaction.setDescription();
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/cancel");
        redirectUrls.setReturnUrl("http://localhost:8080/cart/checkout");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);

        String redirectUrl = "";
        try {
            Payment createdPayment = payment.create(apiContext);
            if (createdPayment != null) {
                List<Links> links = createdPayment.getLinks();
                for (Links link : links) {
                    if (link.getRel().equals("approval_url")) {
                        redirectUrl = link.getHref();
                        break;
                    }
                }
                response.put("status", "success");
                response.put("redirectUrl", redirectUrl);
            }
        } catch(PayPalRESTException e) {
            System.out.println("Error happened during payment creation!");
//            redirectUrl = "/cancel";
            response.put("status", "failure");
//            response.put("redirectUrl", redirectUrl);
        }

        return response;
    }

    public Map<String, Object> executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Map<String, Object> response = new HashMap<>();

        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        try {
            Payment createdPayment = payment.execute(apiContext, paymentExecution);
            if (createdPayment != null){
                response.put("status", "success");
                response.put("payment", createdPayment);
            }
        } catch (PayPalRESTException e) {
            response.put("status", "failure");
            System.err.println(e.getDetails());
        }

        return response;
    }

}
