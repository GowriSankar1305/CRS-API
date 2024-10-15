package com.eazybyts.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eazybyts.boot.entity.Admin;




public interface AdminRepository extends JpaRepository<Admin, Long> {
	
	public Admin findByAdminId(Long adminId);
	public Admin findByEmailId(String emailId);
	public Admin findByMobileNo(String mobileNo);
	public Admin findByUser_UserId(Long userId);
}
