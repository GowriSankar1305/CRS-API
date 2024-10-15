package com.eazybyts.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eazybyts.boot.entity.CarCompany;
import java.util.List;


public interface CarCompanyRepository extends JpaRepository<CarCompany, Long> {

	CarCompany findByCompanyId(Long companyId);
	CarCompany findByCompanyName(String companyName);
	List<CarCompany> findByAdminId(Long adminId);
}
