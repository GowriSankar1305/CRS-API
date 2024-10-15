package com.eazybyts.boot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarDto {
	
	private String carId;
	@NotBlank(message = "model name cannot be empty")
	private String modelName;
	@NotBlank(message = "version cannot be empty")
	private String version;
	@Min(value = 1,message = "no of doors is invalid")
	private Short noOfDoors;
	@Min(value = 4,message = "seating capacity is invalid")
	private Short seatingCapacity;
	@Min(value = 1,message = "fuel tank capacity is invalid")
	private Short fuelTankCapacity;
	@NotBlank(message = "fuel type cannot be empty")
	private String fuelType;
	@NotBlank(message = "transmission type cannot be empty")
	private String transmissionType;
	@Min(value = 1,message = "mileage is invalid")
	private Short mileage;
	@Pattern(regexp = "\\d*\\.\\d+|\\d+\\.\\d*|\\d+",message = "price per hour is invalid")
	private String pricePerHour;
	private String description;
	private String imagePath;
	private AdminDto admin;
	private String companyId;
	private String imageId;
}
