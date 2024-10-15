package com.eazybyts.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eazybyts.boot.entity.CrsUser;

public interface UserRepository extends JpaRepository<CrsUser, Long> {
	
	CrsUser findByUserName(String userName);
}
