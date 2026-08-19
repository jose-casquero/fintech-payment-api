package com.fintech.payment.service;

import com.fintech.payment.request.PaymentRequest;
import com.fintech.payment.response.PaymentResponse;


public interface PaymentService {   
  
    PaymentResponse processPayment(PaymentRequest request);  
    PaymentResponse getPayment(Long id);
}
