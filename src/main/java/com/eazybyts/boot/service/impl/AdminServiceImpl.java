package com.eazybyts.boot.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eazybyts.boot.dto.AdminDto;
import com.eazybyts.boot.dto.ApiResponseDto;
import com.eazybyts.boot.entity.Admin;
import com.eazybyts.boot.enums.UserRoleEnum;
import com.eazybyts.boot.exception.InvalidInputException;
import com.eazybyts.boot.repository.AdminRepository;
import com.eazybyts.boot.service.AdminService;
import com.eazybyts.boot.util.CrsUtility;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private CrsUtility utility;
	
	@Override
	@Transactional
	public ApiResponseDto saveAdmin(AdminDto adminDto) {
 		Admin admin = dtoToEntity(adminDto);
 		admin.setUser(utility.userDtoToEntity(adminDto.getUser(),UserRoleEnum.CUSTOMER));
 		admin.setCreatedTime(LocalDateTime.now());
 		admin.setModifedTime(LocalDateTime.now());
		adminRepository.save(admin);
		return new ApiResponseDto("Admin account created successfully!", 201);
	}

	@Override
	@Transactional
	public AdminDto findAdminById(Long adminId) {
 		Admin admin = adminRepository.findByAdminId(adminId);
 		if(Objects.isNull(admin))	{
 			throw new InvalidInputException(List.of("Admin not found with id: " + adminId));
 		}
		return entityToDto(admin);
	}

	private AdminDto entityToDto(Admin entity)	{
		AdminDto dto = new AdminDto();
		if(Objects.nonNull(entity))	{
			dto.setAdminId(entity.getAdminId().toString());
			if(Objects.nonNull(entity.getAddress()))	{
				dto.setAddress(utility.addrsEntityToDto(entity.getAddress()));
			}
			dto.setEmailId(entity.getEmailId());
			dto.setFirstName(entity.getFirstName());
			dto.setLastName(entity.getLastName());
			dto.setMobileNo(entity.getMobileNo());
			dto.setUser(utility.userEntityToDto(entity.getUser()));
		}
		return dto;
	}
	
	private Admin dtoToEntity(AdminDto dto)	{
		Admin entity = new Admin();
		if(Objects.nonNull(dto))	{
			entity.setAddress(utility.addrsDtoToEntity(dto.getAddress()));
			entity.setEmailId(dto.getEmailId());
			entity.setFirstName(dto.getFirstName());
			entity.setLastName(dto.getLastName());
			entity.setMobileNo(dto.getMobileNo());
		}
		return entity;
	}

	@Override
	@Transactional
	public AdminDto findAdminByUserId(Long userId) {
		Admin admin = adminRepository.findByUser_UserId(userId);
		if(Objects.isNull(admin))	{
			throw new InvalidInputException(List.of("Admin not found with user id: " + userId));
		}
		return entityToDto(admin);
	}
}
