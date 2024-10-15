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
public class AdminDto {

	private String adminId;
	@NotBlank(message = "first name cannot be empty")
	private String firstName;
	@NotBlank(message = "last name cannot be empty")
	private String lastName;
	@NotBlank(message = "mobile no cannot be empty")
	private String mobileNo;
	@Email(message = "email is not valid")
	private String emailId;
	@Valid
	private AddressDto address;
	@Valid
	private UserDto user;
	
}
