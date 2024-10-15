package com.eazybyts.boot.service;

import java.util.List;

import com.eazybyts.boot.dto.ApiResponseDto;
import com.eazybyts.boot.dto.CustomerDto;

public interface CustomerService {

	public ApiResponseDto saveCustomer(CustomerDto customerDto);
	public CustomerDto findCustomer(Long customerId);
	public List<CustomerDto> findCustomersByAdmin(Long adminId);
	public CustomerDto findCustomerByUserId(Long userId);
}
