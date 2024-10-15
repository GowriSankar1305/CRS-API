package com.eazybyts.boot.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.eazybyts.boot.constants.CrsConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tbl_car_company",schema = CrsConstants.DB_SCHEMA)
public class CarCompany {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "carCompanyIdGen")
	@SequenceGenerator(name = "carCompanyIdGen",allocationSize = 1,initialValue = 1000
	,schema = CrsConstants.DB_SCHEMA,sequenceName = "seq_car_company")
	private Long companyId;
	@Column(unique = true)
	private String companyName;
	private String companyLogo;
	@Column(nullable = false)
	private Long adminId;
	private LocalDateTime createdTime;
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "company")
	private List<Car> cars;
}
