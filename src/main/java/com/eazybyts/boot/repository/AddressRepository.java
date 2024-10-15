package com.eazybyts.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eazybyts.boot.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
