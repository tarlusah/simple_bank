package com.vinra.simple_banking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vinra.simple_banking.constant.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;
	private BigDecimal amount;
	private LocalDateTime date;

	@Enumerated(EnumType.STRING)
	private TransactionType type;
}
