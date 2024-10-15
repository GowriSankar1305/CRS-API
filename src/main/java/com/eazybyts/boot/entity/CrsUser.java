package com.eazybyts.boot.entity;

import java.time.LocalDateTime;

import com.eazybyts.boot.constants.CrsConstants;
import com.eazybyts.boot.enums.UserRoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "tbl_crs_user",schema = CrsConstants.DB_SCHEMA)
public class CrsUser {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "userIdGen")
	@SequenceGenerator(name = "userIdGen",allocationSize = 1,initialValue = 1000
	,schema = CrsConstants.DB_SCHEMA,sequenceName = "seq_crs_user")
	private Long userId;
	@Column(unique = true)
	private String userName;
	private String password;
	@Enumerated(EnumType.STRING)
	private UserRoleEnum userRole;
	private LocalDateTime createdTime;
}
