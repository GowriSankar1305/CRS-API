package com.eazybyts.boot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
	private String userId;
	@NotBlank(message = "username cannot be empty")
	private String userName;
	@NotBlank(message = "password cannot be empty")
	private String password;
	@NotBlank(message = "confirm password cannot be empty")
	private String confirmPassword;
	private String userRole;
}
