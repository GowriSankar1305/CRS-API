package com.eazybyts.boot.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.eazybyts.boot.constants.CrsConstants;
import com.eazybyts.boot.enums.IdProofTypeEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tbl_customer",schema = CrsConstants.DB_SCHEMA)
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "customerIdGen")
	@SequenceGenerator(name = "customerIdGen",allocationSize = 1,initialValue = 1000
	,schema = CrsConstants.DB_SCHEMA,sequenceName = "seq_customer")
	private Long customerId;
	private String firstName;
	private String lastname;
	@Column(unique = true)
	private String mobileNo;
	@Column(unique = true)
	private String emailAddress;
	private LocalDate dateOfBirth;
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;
	private String drivingLicenseNo;
	private LocalDate licenseExpiryDate;
	private String licenseImagePath;
	@Enumerated(EnumType.STRING)
	private IdProofTypeEnum idProofType;
	private String idProofNumber;
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "customer")
	private List<Reservation> reservations;
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "admin_id",nullable = false)
	private Admin admin;
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id",nullable = false,unique = true)
	private CrsUser user;
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	private Long createdBy;
	private Long modifiedBy;
}
