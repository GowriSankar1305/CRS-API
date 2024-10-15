package com.eazybyts.boot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDto {
	
	private String addressId;
	private String street;
	@NotBlank(message = "city name cannot be empty")
	private String city;
	@NotBlank(message = "zip ode cannot be empty")
	private String zipCode;
	@NotBlank(message = "state name cannot be empty")
	private String state;
}
