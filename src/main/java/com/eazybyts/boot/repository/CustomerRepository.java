package com.eazybyts.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eazybyts.boot.entity.Customer;
import java.util.List;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Customer findByCustomerId(Long customerId);
	Customer findByEmailAddress(String emailAddress);
	Customer findByMobileNo(String mobileNo);
	List<Customer> findByAdmin_AdminId(Long adminId);
	Customer findByUser_UserId(Long userId);
}
