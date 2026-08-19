package com.fintech.payment.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "The amount cannot be zero")
    @Positive(message = "The amount must be positive.")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "The currency must have 3 characters (e.g., USD)")
    private String currency;

    @NotBlank(message = "The card number is required")
    @Pattern(regexp = "^\\d{16}$", message = "The card number must have 16 digits.")
    private String cardNumber;
}
