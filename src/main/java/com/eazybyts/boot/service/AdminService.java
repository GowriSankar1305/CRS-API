package com.eazybyts.boot.service;

import com.eazybyts.boot.dto.AdminDto;
import com.eazybyts.boot.dto.ApiResponseDto;

public interface AdminService {
	public ApiResponseDto saveAdmin(AdminDto adminDto);
	public AdminDto findAdminById(Long adminId);
	public AdminDto findAdminByUserId(Long userId);
}
