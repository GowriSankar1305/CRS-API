package com.eazybyts.boot.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.eazybyts.boot.constants.CrsConstants;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tbl_admin",schema = CrsConstants.DB_SCHEMA)
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "adminIdGen")
	@SequenceGenerator(name = "adminIdGen",allocationSize = 1,initialValue = 1000
	,schema = CrsConstants.DB_SCHEMA,sequenceName = "seq_admin")
	private Long adminId;
	private String firstName;
	private String lastName;
	@Column(unique = true)
	private String mobileNo;
	@Column(unique = true)
	private String emailId;
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id",nullable = false,unique = true)
	private CrsUser user;
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "admin")
	private List<Car> cars;
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "admin")
	private List<Customer> customers;
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "admin")
	private List<Reservation> reservations;
	private LocalDateTime createdTime;
	private LocalDateTime modifedTime;
}
