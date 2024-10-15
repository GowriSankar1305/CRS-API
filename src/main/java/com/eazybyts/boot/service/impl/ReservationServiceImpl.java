package com.eazybyts.boot.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.eazybyts.boot.dto.ApiResponseDto;
import com.eazybyts.boot.dto.ReservationDto;
import com.eazybyts.boot.entity.Reservation;
import com.eazybyts.boot.exception.InvalidInputException;
import com.eazybyts.boot.repository.AdminRepository;
import com.eazybyts.boot.repository.CarRepository;
import com.eazybyts.boot.repository.CustomerRepository;
import com.eazybyts.boot.repository.ReservationRepository;
import com.eazybyts.boot.service.ReservationService;

@Service 
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private CarRepository carRepository;
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	@Transactional
	public ApiResponseDto addReservation(ReservationDto reservationDto) {
		reservationRepository.save(dtoToEntity(reservationDto));
		return new ApiResponseDto("Reservation added successfully", 200);
	}

	private Reservation dtoToEntity(ReservationDto dto)	{
		Reservation entity = new Reservation();
		entity.setAdmin(adminRepository.findByAdminId(Long.parseLong(dto.getAdminId())));
		entity.setCar(carRepository.findByCarId(Long.parseLong(dto.getCarId())));
		entity.setCustomer(customerRepository.findByCustomerId(Long.parseLong(dto.getCustomerId())));
		entity.setComment(dto.getComment());
		entity.setPickupTime(LocalDateTime.parse(dto.getPickupTime()));
		entity.setReturnTime(LocalDateTime.parse(dto.getReturnTime()));
		entity.setCreatedTime(LocalDateTime.now());
		entity.setModifiedTime(LocalDateTime.now());
		entity.setIsAdminReservation(dto.isAdminReservation());
		entity.setIsCustomerReservation(dto.isCustomerReservation());
		if(dto.isAdminReservation())	{
			entity.setCreatedBy(Long.parseLong(dto.getAdminId()));
			entity.setModifiedBy(Long.parseLong(dto.getAdminId()));
		}
		else	{
			entity.setCreatedBy(Long.parseLong(dto.getCustomerId()));
			entity.setModifiedBy(Long.parseLong(dto.getCustomerId()));
		}
		return entity;
	}
	
	private ReservationDto entityToDto(Reservation entity)	{
		ReservationDto dto = new ReservationDto();
		dto.setReservationId(entity.getReservationId());
		dto.setPickupTime(entity.getPickupTime().toString());
		dto.setReturnTime(entity.getReturnTime().toString());
		dto.setComment(entity.getComment());
		dto.setCustomerReservation(entity.getIsCustomerReservation());
		dto.setAdminReservation(entity.getIsAdminReservation());
		return dto;		
	}
	
	@Override
	@Transactional
	public List<ReservationDto> getAllReservationsOfCustomer(Long customerId) {
		List<ReservationDto> dtoList = new ArrayList<>();
		List<Reservation> reservations = reservationRepository.findByCustomer_CustomerId(customerId);
		if(!CollectionUtils.isEmpty(reservations))	{
			reservations.forEach(entity -> dtoList.add(entityToDto(entity)));
		}
		return dtoList;
	}

	@Override
	@Transactional
	public ReservationDto findReservationDetaails(Long reservationId) {
		Reservation reservation = reservationRepository.findByReservationId(reservationId);
		if(Objects.isNull(reservation))	{
			throw new InvalidInputException(List.of("invalid reservation id. No record found"));
		}
		return entityToDto(reservation);
	}

}
