package com.eazybyts.boot.dto;

import lombok.Data;

@Data
public class ReservationDto {
	private Long reservationId;
	private String carId;
	private String customerId;
	private String adminId;
	private String comment;
	private String pickupTime;
	private String returnTime;
	private boolean isCustomerReservation;
	private boolean isAdminReservation;
}
