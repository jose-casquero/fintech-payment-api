package com.fintech.payment.mapper;

import com.fintech.payment.entity.PaymentEntity;
import com.fintech.payment.request.PaymentRequest;
import com.fintech.payment.response.PaymentResponse;
import com.fintech.payment.util.CardUtil;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;


@Component
public class PaymentMapper {


    public PaymentEntity toEntity(PaymentRequest request) {
        return PaymentEntity.builder()
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .cardNumberMasked(CardUtil.maskCardNumber(request.getCardNumber()))
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
    }

 
    public PaymentResponse toResponse(PaymentEntity entity) {
        return PaymentResponse.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .status(entity.getStatus())
                .transactionDate(entity.getCreatedAt())
                .build();
    }
}
