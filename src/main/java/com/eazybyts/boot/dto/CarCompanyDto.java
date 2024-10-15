package com.eazybyts.boot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CarCompanyDto {
	
	private String companyId;
	@NotBlank(message = "company name cannot be empty")
	private String companyName;
	private String companyLogoPath;
	private String adminId;
}
