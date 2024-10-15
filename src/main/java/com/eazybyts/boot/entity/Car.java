package com.eazybyts.boot.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.eazybyts.boot.constants.CrsConstants;
import com.eazybyts.boot.enums.FuelTypeEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tbl_car",schema = CrsConstants.DB_SCHEMA)
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "carIdGen")
	@SequenceGenerator(name = "carIdGen",allocationSize = 1,initialValue = 1000
	,schema = CrsConstants.DB_SCHEMA,sequenceName = "seq_car")
	private Long carId;
	private String modelName;
	private String version;
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "company_id",nullable = false)
	private CarCompany company;
	private Short noOfDoors;
	private Short seatingCapacity;
	private Short fuelTankCapacity;
	@Enumerated(EnumType.STRING)
	private FuelTypeEnum fuelType;
	private String transmissionType;
	private Short mileage;
	private BigDecimal pricePerHour;
	private String description;
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "image_id")
	private CarImage image;
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id",nullable = false)
	private Admin admin;
	private LocalDateTime createdTime;
	private LocalDateTime modifiedTime;
}
