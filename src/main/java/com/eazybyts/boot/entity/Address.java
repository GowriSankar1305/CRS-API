package com.eazybyts.boot.entity;

import com.eazybyts.boot.constants.CrsConstants;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tbl_address",schema = CrsConstants.DB_SCHEMA)
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "addrsIdGen")
	@SequenceGenerator(name = "addrsIdGen",allocationSize = 1,initialValue = 1000
	,schema = CrsConstants.DB_SCHEMA,sequenceName = "seq_address")
	private Long addressId;
	private String street;
	private String city;
	private String zipCode;
	private String state;
}
