package com.eazybyts.boot.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.eazybyts.boot.constants.CrsConstants;
import com.eazybyts.boot.enums.PaymentStatusEnum;
import com.eazybyts.boot.enums.PaymentTypeEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tbl_transaction",schema = CrsConstants.DB_SCHEMA)
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "txnIdGen")
	@SequenceGenerator(name = "txnIdGen",allocationSize = 1,initialValue = 1000
	,schema = CrsConstants.DB_SCHEMA,sequenceName = "seq_transaction")
	private Long transactionId;
	@Enumerated(EnumType.STRING)
	private PaymentTypeEnum  paymentType;
	private String referenceId;
	private BigDecimal totalAmount;
	private BigDecimal gst;
	private BigDecimal baseAmount;
	private LocalDate transactionDate;
	@Enumerated(EnumType.STRING)
	private PaymentStatusEnum paymentStatus;
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id",nullable = false)
	private Reservation reservation;
	private LocalDateTime createdTime;
}
