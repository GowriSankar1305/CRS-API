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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eazybyts.boot.config.CustomUserDetails;
import com.eazybyts.boot.constants.CrsConstants;
import com.eazybyts.boot.dto.AdminDto;
import com.eazybyts.boot.dto.ApiResponseDto;
import com.eazybyts.boot.dto.CarCompanyDto;
import com.eazybyts.boot.dto.CarDto;
import com.eazybyts.boot.dto.ImageDto;
import com.eazybyts.boot.dto.UserDto;
import com.eazybyts.boot.exception.InvalidInputException;
import com.eazybyts.boot.service.AdminService;
import com.eazybyts.boot.service.CarService;
import com.eazybyts.boot.util.CrsUtility;
import com.eazybyts.boot.util.JwtUtility;
import com.eazybyts.boot.util.ThreadLocalUtility;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/crs/admin/")
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtility jwtUtility;
	@Autowired
	private CarService carService;
	@Autowired
	private CrsUtility crsUtility;
	
	@PostMapping("login")
	public ApiResponseDto loginAdmin(@RequestBody UserDto userDto) throws Exception {
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

	@PostMapping("register")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponseDto registerAdmin(@RequestBody @Valid AdminDto adminDto) {
		return adminService.saveAdmin(adminDto);
	}
	
	@PostMapping("car/addOrUpdate")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponseDto addOrUpdateCar(@RequestBody @Valid CarDto carDto)	{
		return carService.addOrUpdateCar(carDto);
	}

	@PostMapping("car/find")
	@ResponseStatus(code = HttpStatus.OK)
	public CarDto findACar(@RequestBody Map<String, String> payload)	{
		if(!crsUtility.isNumber(payload.get("id")))	{
			throw new InvalidInputException(List.of("Invalid card id received"));
		}
		return carService.findCarById(Long.parseLong(payload.get("id")));
	}
	
	@PostMapping("cars")
	@ResponseStatus(code = HttpStatus.OK)
	public List<CarDto> findAllcars()	{
		AdminDto admin = adminService.findAdminByUserId((Long)ThreadLocalUtility.get().get(CrsConstants.ID));
		return carService.findAllCarsOfAdmin(Long.parseLong(admin.getAdminId()));
	}
	
	@PostMapping("carCompany/add")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponseDto addCarCompany(@RequestBody @Valid CarCompanyDto carCompanyDto)	{
		return carService.saveCarCompany(carCompanyDto);
	}
	
	@PostMapping("carCompanies")
	@ResponseStatus(code = HttpStatus.OK)
	public List<CarCompanyDto> findAllCarCompanies()	{
		AdminDto adminDto = adminService.findAdminByUserId(
				(Long)ThreadLocalUtility.get().get(CrsConstants.ID));
		return carService.findAllCarBrandsOfAdmin(Long.parseLong(adminDto.getAdminId()));
	}
	
	@PostMapping(value = "car/addImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiResponseDto uploadImage(@RequestParam("carImageFile") MultipartFile carImage,
			@RequestParam("carId") String carId)	{
		log.info("car id received-----------> {}",carId);
		return carService.addCardImage(carImage, carId);
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
}
