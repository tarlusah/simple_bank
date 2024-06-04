package com.vinra.simple_banking.controller.dto;

import com.vinra.simple_banking.constant.TransactionType;
import com.vinra.simple_banking.validation.ValueOfEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

	@NotNull(message = "transactionType is mandatory")
	@ValueOfEnum(enumClass = TransactionType.class, message = "transaction type must (WITHDRAW/DEPOSIT)")
	private String transactionType;

	@NotNull(message = "amount is mandatory")
	@DecimalMin(value = "0.0", inclusive = false)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal amount;
}
