package com.eazybyts.boot.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.eazybyts.boot.constants.CrsConstants;
import com.eazybyts.boot.dto.ApiResponseDto;
import com.eazybyts.boot.dto.CarCompanyDto;
import com.eazybyts.boot.dto.CarDto;
import com.eazybyts.boot.dto.ImageDto;
import com.eazybyts.boot.entity.Admin;
import com.eazybyts.boot.entity.Car;
import com.eazybyts.boot.entity.CarCompany;
import com.eazybyts.boot.entity.CarImage;
import com.eazybyts.boot.entity.Reservation;
import com.eazybyts.boot.enums.FuelTypeEnum;
import com.eazybyts.boot.exception.InvalidInputException;
import com.eazybyts.boot.repository.AdminRepository;
import com.eazybyts.boot.repository.CarCompanyRepository;
import com.eazybyts.boot.repository.CarRepository;
import com.eazybyts.boot.repository.ReservationRepository;
import com.eazybyts.boot.service.CarService;
import com.eazybyts.boot.service.FileStorageService;
import com.eazybyts.boot.util.CrsUtility;
import com.eazybyts.boot.util.ThreadLocalUtility;

@Service
public class CarServiceImpl implements CarService {

	@Autowired
	private CarRepository carRepository;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private CarCompanyRepository carCompanyRepository;
	@Autowired
	private CrsUtility utility;
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private FileStorageService fileStorageService;
	@Value("${image.file_upload.directory}")
	private String imagePath;

	@Override
	@Transactional
	public ApiResponseDto addOrUpdateCar(CarDto carDto) {
		Car car = dtoToEntity(carDto);
		String msg = "";
		int code = 0;
		if (StringUtils.hasText(carDto.getCarId()) && utility.isNumber(carDto.getCarId())) {
			car.setCarId(Long.parseLong(carDto.getCarId()));
			car.setModifiedTime(LocalDateTime.now());
			msg = "Car updated successfully";
			code = 200;
		} else {
			car.setCreatedTime(LocalDateTime.now());
			car.setModifiedTime(LocalDateTime.now());
			msg = "Car added successfully";
			code = 201;
		}
		carRepository.save(car);
		return new ApiResponseDto(msg, code);
	}

	@Override
	public CarDto findCarById(Long carId) {
		Car car = carRepository.findByCarId(carId);
		if (Objects.isNull(car)) {
			throw new InvalidInputException(List.of("Card not found with id: " + carId));
		}
		return entityToDto(car);
	}

	@Override
	public List<CarDto> findAllCarsOfAdmin(Long adminId) {
		List<CarDto> dtoList = new ArrayList<>();
		List<Car> cars = carRepository.findByAdmin_AdminId(adminId);
		if (!CollectionUtils.isEmpty(cars)) {
			cars.forEach(entity -> dtoList.add(entityToDto(entity)));
		}
		return dtoList;
	}

	private CarDto entityToDto(Car entity) {
		CarDto dto = new CarDto();
		dto.setCarId(entity.getCarId().toString());
		dto.setDescription(entity.getDescription());
		dto.setFuelTankCapacity(entity.getFuelTankCapacity());
		dto.setFuelType(entity.getFuelType().name());
		dto.setMileage(entity.getMileage());
		dto.setModelName(entity.getModelName());
		dto.setNoOfDoors(entity.getNoOfDoors());
		dto.setPricePerHour(entity.getPricePerHour().toString());
		dto.setSeatingCapacity(entity.getSeatingCapacity());
		dto.setTransmissionType(entity.getTransmissionType());
		dto.setVersion(entity.getVersion());
		dto.setCompanyId(entity.getCompany().getCompanyId().toString());
		if (Objects.nonNull(entity.getImage())) {
			dto.setImageId(entity.getImage().getImageId().toString());
		}
		return dto;
	}

	private Car dtoToEntity(CarDto dto) {
		Car car = new Car();
		if (Objects.nonNull(dto)) {
			car.setModelName(dto.getModelName());
			car.setVersion(dto.getVersion());
			car.setAdmin(adminRepository.findByUser_UserId((Long) ThreadLocalUtility.get().get(CrsConstants.ID)));
			car.setCompany(carCompanyRepository.findByCompanyId(Long.parseLong(dto.getCompanyId())));
			car.setDescription(dto.getDescription());
			car.setFuelTankCapacity(dto.getFuelTankCapacity());
			car.setFuelType(FuelTypeEnum.valueOf(dto.getFuelType()));
			car.setMileage(dto.getMileage());
			car.setModelName(dto.getModelName());
			car.setNoOfDoors(dto.getNoOfDoors());
			car.setPricePerHour(new BigDecimal(dto.getPricePerHour()));
			car.setSeatingCapacity(dto.getSeatingCapacity());
			car.setTransmissionType(dto.getTransmissionType());
			car.setVersion(dto.getVersion());
		}
		return car;
	}

	@Override
	public ApiResponseDto saveCarCompany(CarCompanyDto carCompanyDto) {
		Admin admin = adminRepository.findByUser_UserId((Long) ThreadLocalUtility.get().get(CrsConstants.ID));
		carCompanyDto.setCompanyName(carCompanyDto.getCompanyName().toUpperCase());
		CarCompany company = carCompanyRepository.findByCompanyName(carCompanyDto.getCompanyName());
		if (Objects.nonNull(company)) {
			throw new InvalidInputException(List.of("company " + carCompanyDto.getCompanyName() + " already exists"));
		}
		CarCompany newCompany = new CarCompany();
		newCompany.setAdminId(admin.getAdminId());
		newCompany.setCompanyLogo(carCompanyDto.getCompanyLogoPath());
		newCompany.setCompanyName(carCompanyDto.getCompanyName());
		newCompany.setCreatedTime(LocalDateTime.now());
		carCompanyRepository.save(newCompany);
		return new ApiResponseDto("Company created successfully", 201);
	}

	@Override
	public List<CarCompanyDto> findAllCarBrandsOfAdmin(Long adminId) {
		List<CarCompanyDto> dtoList = new ArrayList<>();
		List<CarCompany> companies = carCompanyRepository.findByAdminId(adminId);
		if (!CollectionUtils.isEmpty(companies)) {
			companies.forEach(entity -> dtoList.add(carCompanyEntityToDto(entity)));
		}
		return dtoList;
	}

	private CarCompanyDto carCompanyEntityToDto(CarCompany entity) {
		CarCompanyDto dto = new CarCompanyDto();
		dto.setCompanyId(entity.getCompanyId().toString());
		dto.setCompanyName(entity.getCompanyName());
		return dto;
	}

	@Override
	@Transactional
	public ApiResponseDto addCardImage(MultipartFile file, String carId) {
		ApiResponseDto response = new ApiResponseDto();
		CarImage imageObject = fileStorageService.saveFile(file, imagePath);
		if (Objects.isNull(imageObject)) {
			response.setStatusCode(500);
			response.setMessage("Failed to upload image. internal error...");
		}
		Car car = carRepository.findByCarId(Long.parseLong(carId));
		car.setImage(imageObject);
		car = carRepository.save(car);
		response.setMessage("Image uploaded successfully!");
		response.setStatusCode(200);
		response.setImageId(car.getImage().getImageId().toString());
		return response;
	}

	@Override
	@Transactional
	public ImageDto downloadCarImage(Long imageId) {
		ImageDto response = new ImageDto();
		Car car = carRepository.findByImage_ImageId(imageId);
		if (Objects.isNull(car)) {
			throw new InvalidInputException(List.of("invalid image id..."));
		}
		byte[] content = fileStorageService.retreiveFile(car.getImage().getImagePath());
		if (Objects.isNull(content)) {
			throw new InvalidInputException(List.of("Unable to retreive image..."));
		}
		response.setContent(content);
		response.setMimeType(car.getImage().getMimeType());
		return response;
	}

	@Override
	@Transactional
	public List<CarDto> fetchAvailableCars(String fromDate, String toDate, Long adminId) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		List<CarDto> dtoList = new ArrayList<>();
		List<Car> cars = carRepository.findByAdmin_AdminId(adminId);
		cars = new CopyOnWriteArrayList<>(cars);

		if (!CollectionUtils.isEmpty(cars)) {
			List<Reservation> reservations = reservationRepository.findReservationOfAdminBetweenDates
					(LocalDateTime.parse(fromDate,fmt), LocalDateTime.parse(toDate,fmt), adminId);
			if (!CollectionUtils.isEmpty(reservations)) {
				List<Car> bookedCars = reservations.stream().map(res -> res.getCar()).collect(Collectors.toList());
				cars.removeAll(bookedCars);
			}
		}
		cars.forEach(entity -> dtoList.add(entityToDto(entity)));
		return dtoList;
	}
}
