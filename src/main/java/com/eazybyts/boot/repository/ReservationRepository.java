package com.eazybyts.boot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eazybyts.boot.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	Reservation findByReservationId(Long reservationId);
	List<Reservation> findByCustomer_CustomerId(Long customerId);
	@Query("from Reservation r where r.admin.adminId =:adminId and (r.pickupTime >= :fromDate and r.returnTime <= :toDate)")
	List<Reservation> findReservationOfAdminBetweenDates(LocalDateTime fromDate,LocalDateTime toDate,Long adminId);
}
