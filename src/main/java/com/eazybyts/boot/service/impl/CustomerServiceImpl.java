package com.eazybyts.boot.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.eazybyts.boot.dto.ApiResponseDto;
import com.eazybyts.boot.dto.CustomerDto;
import com.eazybyts.boot.dto.DateDto;
import com.eazybyts.boot.entity.Customer;
import com.eazybyts.boot.enums.IdProofTypeEnum;
import com.eazybyts.boot.enums.UserRoleEnum;
import com.eazybyts.boot.exception.InvalidInputException;
import com.eazybyts.boot.repository.AdminRepository;
import com.eazybyts.boot.repository.CustomerRepository;
import com.eazybyts.boot.service.CustomerService;
import com.eazybyts.boot.util.CrsUtility;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CrsUtility utility;
	@Autowired
	private AdminRepository adminRepository;
	
	@Override
	@Transactional
	public ApiResponseDto saveCustomer(CustomerDto customerDto) {
		Customer customer = dtoToEntity(customerDto);
		customer.setUser(utility.userDtoToEntity(customerDto.getUser(), UserRoleEnum.CUSTOMER));
		customer.setCreatedTime(LocalDateTime.now());
		customer.setUpdatedTime(LocalDateTime.now());
		customerRepository.save(customer);
		return new ApiResponseDto("Customer created successfully!", 201);
	}

	@Override
	@Transactional
	public CustomerDto findCustomer(Long customerId) {
 		Customer customer =  customerRepository.findByCustomerId(customerId);
 		if(Objects.isNull(customer))	{
 			throw new InvalidInputException(List.of("customer not found with id: " + customerId));
 		}
		return entityToDto(customer);
	}

	@Override
	@Transactional
	public List<CustomerDto> findCustomersByAdmin(Long adminId) {
		List<CustomerDto> dtoList = new ArrayList<>();
 		List<Customer> customers = customerRepository.findByAdmin_AdminId(adminId);
 		if(!CollectionUtils.isEmpty(customers))	{
 			customers.forEach(entity -> dtoList.add(entityToDto(entity)));
 		}
		return dtoList;
	}
	
	private CustomerDto entityToDto(Customer entity)	{
		CustomerDto dto = new CustomerDto();
		dto.setAddress(utility.addrsEntityToDto(entity.getAddress()));
		dto.setCustomerId(entity.getCustomerId().toString());
		dto.setDrivingLicenseNo(entity.getDrivingLicenseNo());
		dto.setEmailAddress(entity.getEmailAddress());
		dto.setFirstName(entity.getFirstName());
		dto.setIdProofType(entity.getIdProofType().name());
		dto.setLastname(entity.getLastname());
		if(Objects.nonNull(entity.getLicenseExpiryDate()))	{
			LocalDate date = entity.getLicenseExpiryDate();
			dto.setLicenseExpiryDate(new DateDto(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));	
		}
		dto.setLicenseImagePath(entity.getLicenseImagePath());
		dto.setMobileNo(entity.getMobileNo());
		dto.setUser(utility.userEntityToDto(entity.getUser()));
		return dto;
	}
	
	private Customer dtoToEntity(CustomerDto dto)	{
		Customer entity = new Customer();
		entity.setAddress(utility.addrsDtoToEntity(dto.getAddress()));
		entity.setAdmin(adminRepository.findByAdminId(Long.parseLong(dto.getAdminId())));
		if(Objects.nonNull(dto.getDateOfBirth()))	{
			DateDto date = dto.getDateOfBirth();
			entity.setDateOfBirth(LocalDate.of(date.getYear(), date.getMonth(), date.getDate()));	
		}
		entity.setDrivingLicenseNo(dto.getDrivingLicenseNo());
		entity.setEmailAddress(dto.getEmailAddress());
		entity.setFirstName(dto.getFirstName());
		entity.setLastname(dto.getLastname());
		if(Objects.nonNull(dto.getLicenseExpiryDate()))		{
			DateDto date = dto.getLicenseExpiryDate();
			entity.setLicenseExpiryDate(LocalDate.of(date.getYear(), date.getMonth(), date.getDate()));
		}
		entity.setMobileNo(dto.getMobileNo());
		entity.setIdProofType(IdProofTypeEnum.valueOf(dto.getIdProofType()));
		entity.setIdProofNumber(dto.getIdProofNumber());
		return entity;
	}

	@Override
	@Transactional
	public CustomerDto findCustomerByUserId(Long userId) {
		Customer customer = customerRepository.findByUser_UserId(userId);
		if(Objects.isNull(customer))	{
			throw new InvalidInputException(List.of("Invalid user id"));
		}
		return entityToDto(customer);
	}
}
