package com.eazybyts.boot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eazybyts.boot.entity.Car;


public interface CarRepository extends JpaRepository<Car, Long> {
	Car findByCarId(Long carId);
	List<Car> findByAdmin_AdminId(Long adminId);
	Car findByImage_ImageId(Long imageId);
}
