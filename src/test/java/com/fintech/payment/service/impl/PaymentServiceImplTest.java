package com.fintech.payment.service.impl;

import com.fintech.payment.entity.PaymentEntity;
import com.fintech.payment.mapper.PaymentMapper;
import com.fintech.payment.repository.PaymentRepository;
import com.fintech.payment.request.PaymentRequest;
import com.fintech.payment.response.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentRequest request;
    private PaymentEntity entity;
    private PaymentResponse response;

 
    @BeforeEach
    void setUp() {
        request = PaymentRequest.builder()
                .amount(new BigDecimal("100.50"))
                .currency("USD")
                .cardNumber("1234567812345678")
                .build();

        entity = PaymentEntity.builder()
                .id(1L)
                .amount(new BigDecimal("100.50"))
                .currency("USD")
                .cardNumberMasked("************5678")
                .status("APPROVED")
                .createdAt(LocalDateTime.now())
                .build();

        response = PaymentResponse.builder()
                .id(1L)
                .amount(new BigDecimal("100.50"))
                .currency("USD")
                .status("APPROVED")
                .build();
    }

 
    @Test
    void processPayment_Success() {
        when(paymentMapper.toEntity(any(PaymentRequest.class))).thenReturn(entity);
        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(entity);
        when(paymentMapper.toResponse(any(PaymentEntity.class))).thenReturn(response);

        PaymentResponse result = paymentService.processPayment(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("APPROVED", result.getStatus());
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }

 
    @Test
    void processPayment_DatabaseError_ThrowsException() {
        when(paymentMapper.toEntity(any(PaymentRequest.class))).thenReturn(entity);
        when(paymentRepository.save(any(PaymentEntity.class))).thenThrow(new RuntimeException("DB down"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.processPayment(request));        
        assertEquals("Internal error processing payment", ex.getMessage());
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }

   
    @Test
    void getPayment_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(paymentMapper.toResponse(any(PaymentEntity.class))).thenReturn(response);
        PaymentResponse result = paymentService.getPayment(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

 
    @Test
    void getPayment_NotFound_ThrowsException() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.getPayment(99L));
        assertEquals("Payment not found", ex.getMessage());
    }
}
