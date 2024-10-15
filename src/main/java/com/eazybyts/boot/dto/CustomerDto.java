package com.eazybyts.boot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDto {
	
	private String customerId;
	@NotBlank(message = "first name cannot be empty")
	private String firstName;
	@NotBlank(message = "last name cannot be empty")
	private String lastname;
	@NotBlank(message = "mobile no cannot be empty")
	private String mobileNo;
	@Email(message = "email address is invalid")
	private String emailAddress;
	@Valid
	private AddressDto address;
	private String drivingLicenseNo;
	private DateDto licenseExpiryDate;
	private DateDto dateOfBirth;
	private String licenseImagePath;
	@NotBlank(message = "id proof cannot be empty")
	private String idProofType;
	@NotBlank(message = "id proof number cannot be empty")
	private String idProofNumber;
	@Valid
	private UserDto user;
	private String adminId;
	private String createdBy;
}
