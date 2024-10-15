package com.eazybyts.boot.service;

import java.util.List;

import com.eazybyts.boot.dto.ApiResponseDto;
import com.eazybyts.boot.dto.ReservationDto;

public interface ReservationService {
	public ApiResponseDto addReservation(ReservationDto reservationDto);
	public List<ReservationDto> getAllReservationsOfCustomer(Long customerId);
	public ReservationDto findReservationDetaails(Long reservationId);
}
