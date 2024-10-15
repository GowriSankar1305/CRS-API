package com.eazybyts.boot.entity;

import java.time.LocalDateTime;

import com.eazybyts.boot.constants.CrsConstants;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
@Table(name = "tbl_reservation",schema = CrsConstants.DB_SCHEMA)
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "reservationIdGen")
	@SequenceGenerator(name = "reservationIdGen",allocationSize = 1,initialValue = 1000
	,schema = CrsConstants.DB_SCHEMA,sequenceName = "seq_reservation")
	private Long reservationId;
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "car_id",nullable = false)
	private Car car;
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id",nullable = false)
	private Customer customer;
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id",nullable = false)
	private Admin admin;
	private String comment;
	private LocalDateTime pickupTime;
	private LocalDateTime returnTime;
	private LocalDateTime createdTime;
	private LocalDateTime modifiedTime;
	private Long createdBy;
	private Long modifiedBy;
	private Boolean isCustomerReservation;
	private Boolean isAdminReservation;
}
