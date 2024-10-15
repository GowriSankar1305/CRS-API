package com.eazybyts.boot.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eazybyts.boot.config.CustomUserDetails;
import com.eazybyts.boot.constants.CrsConstants;
import com.eazybyts.boot.dto.ApiResponseDto;
import com.eazybyts.boot.dto.CarDto;
import com.eazybyts.boot.dto.CustomerDto;
import com.eazybyts.boot.dto.ImageDto;
import com.eazybyts.boot.dto.ReservationDto;
import com.eazybyts.boot.dto.UserDto;
import com.eazybyts.boot.exception.InvalidInputException;
import com.eazybyts.boot.service.CarService;
import com.eazybyts.boot.service.CustomerService;
import com.eazybyts.boot.service.ReservationService;
import com.eazybyts.boot.util.JwtUtility;
import com.eazybyts.boot.util.ThreadLocalUtility;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/crs/customer/")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private ReservationService reservationService;
	@Autowired
	private JwtUtility jwtUtility;
	@Autowired
	private CarService carService;
	
	@PostMapping("register")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponseDto registerCustomer(@RequestBody @Valid CustomerDto customerDto)	{
		return customerService.saveCustomer(customerDto);
	}
	
	@PostMapping("login")
	public ApiResponseDto loginCustomer(@RequestBody UserDto userDto) throws Exception {
		if (!StringUtils.hasText(userDto.getUserName()) || !StringUtils.hasText(userDto.getPassword())) {
			throw new InvalidInputException(List.of("Invalid credentials"));
		}
		log.info("***** authenticating user *****");
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDto.getUserName(), userDto.getPassword());
		Authentication authResponse = authenticationManager.authenticate(authentication);
		log.info("authorities-----> {}", authResponse.getAuthorities());
		log.info("name-----> {}", authResponse.getName());
		CustomUserDetails userDetails = (CustomUserDetails) authResponse.getPrincipal();
		String token = jwtUtility.generateToken(userDetails.getUsername(), userDetails.getUserId(),
				userDetails.getRole());
		ApiResponseDto loginResponseDto = new ApiResponseDto();
		loginResponseDto.setToken(token);
		return loginResponseDto;
	}
	
	@PostMapping("searchCars")
	public List<CarDto> getAvaialableCarsForBooking(@RequestBody Map<String, String> payload)	{
		return carService.fetchAvailableCars(payload.get("pickupTime")
				,payload.get("returnTime"),Long.parseLong(payload.get("adminId")));
	}
	
	@GetMapping(value = "car/image/download/{imageId}")
	public ResponseEntity<byte[]> downloadCarImage(@PathVariable String imageId)	{
		log.info("received image id to donwload------> {}", imageId);
		ImageDto dto = carService.downloadCarImage(Long.parseLong(imageId));
		return ResponseEntity
				.ok()
				.contentType(MediaType.parseMediaType(dto.getMimeType()))
				.body(dto.getContent());
	}
	
	@PostMapping("bookCar")
	public ApiResponseDto addReservation(@RequestBody @Valid ReservationDto reservationDto)	{
		Long userId = (Long)ThreadLocalUtility.get().get(CrsConstants.ID);
		CustomerDto customerDto = customerService.findCustomerByUserId(userId);
		reservationDto.setCustomerId(customerDto.getCustomerId());
		reservationDto.setAdminId(customerDto.getAdminId());
		reservationDto.setCustomerReservation(true);
		reservationDto.setAdminReservation(false);
		return reservationService.addReservation(reservationDto);
	}
}
