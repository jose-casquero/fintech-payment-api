package com.fintech.payment.service.impl;

import com.fintech.payment.entity.PaymentEntity;
import com.fintech.payment.mapper.PaymentMapper;
import com.fintech.payment.repository.PaymentRepository;
import com.fintech.payment.request.PaymentRequest;
import com.fintech.payment.response.PaymentResponse;
import com.fintech.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

  
    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        log.info("Initiating payment processing by amount: {} {}", request.getAmount(), request.getCurrency());
        
        try {
            PaymentEntity entity = paymentMapper.toEntity(request);            
            entity.setStatus("APPROVED");            
            PaymentEntity savedEntity = paymentRepository.save(entity);
            log.info("Payment processed and saved successfully with ID: {}", savedEntity.getId());            
            return paymentMapper.toResponse(savedEntity);

        } catch (Exception e) {
            log.error("Critical error processing payment: {}", e.getMessage(), e);
            throw new RuntimeException("Internal error processing payment");
        }
    }

   
    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Long id) {
        log.info("Looking for payment with ID: {}", id);
        
        PaymentEntity entity = paymentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Payment not found for ID: {}", id);
                    return new RuntimeException("Payment not found");
                });
                
        return paymentMapper.toResponse(entity);
    }
}
