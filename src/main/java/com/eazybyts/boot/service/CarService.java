package com.eazybyts.boot.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eazybyts.boot.dto.ApiResponseDto;
import com.eazybyts.boot.dto.CarCompanyDto;
import com.eazybyts.boot.dto.CarDto;
import com.eazybyts.boot.dto.ImageDto;

public interface CarService {
	public ApiResponseDto addOrUpdateCar(CarDto carDto);
	public CarDto findCarById(Long carId);
	public List<CarDto> findAllCarsOfAdmin(Long adminId);
	public ApiResponseDto saveCarCompany(CarCompanyDto carCompanyDto);
	public List<CarCompanyDto> findAllCarBrandsOfAdmin(Long adminId);
	public ApiResponseDto addCardImage(MultipartFile file,String carId);
	public ImageDto downloadCarImage(Long imageId);
	public List<CarDto> fetchAvailableCars(String fromDate,String toDate,Long adminId); 
}
