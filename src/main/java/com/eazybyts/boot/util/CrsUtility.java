package com.eazybyts.boot.util;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.eazybyts.boot.dto.AddressDto;
import com.eazybyts.boot.dto.UserDto;
import com.eazybyts.boot.entity.Address;
import com.eazybyts.boot.entity.CrsUser;
import com.eazybyts.boot.enums.UserRoleEnum;

@Component
public class CrsUtility {

	private static final String noregEx = "^[0-9]+";
	Pattern noPattern = Pattern.compile(noregEx);
	private static Map<String, String> mimeTypeMap = null;
	static {
		mimeTypeMap = new ConcurrentHashMap<>();
		mimeTypeMap.put("jpg", "image/jpeg");
		mimeTypeMap.put("jpeg", "image/jpeg");
		mimeTypeMap.put("png", "image/png");
		mimeTypeMap.put("gif", "image/gif");
		mimeTypeMap.put("bmp", "image/bmp");
		mimeTypeMap.put("tiff", "image/tiff");
		mimeTypeMap.put("tif", "image/tiff");
		mimeTypeMap.put("webp", "image/webp");
		mimeTypeMap.put("ico", "image/x-icon");
		mimeTypeMap.put("svg", "image/svg+xml");
		mimeTypeMap.put("jfif", "image/jpeg");
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	public AddressDto addrsEntityToDto(Address entity) {
		AddressDto dto = new AddressDto();
		if (Objects.nonNull(entity)) {
			dto.setAddressId(entity.getAddressId().toString());
			dto.setCity(entity.getCity());
			dto.setState(entity.getState());
			dto.setStreet(entity.getStreet());
			dto.setZipCode(entity.getZipCode());
		}
		return dto;
	}

	public Address addrsDtoToEntity(AddressDto addressDto) {
		Address entity = new Address();
		if (Objects.nonNull(addressDto)) {
			entity.setCity(addressDto.getCity());
			entity.setState(addressDto.getState());
			entity.setStreet(addressDto.getStreet());
			entity.setZipCode(addressDto.getZipCode());
		}
		return entity;
	}

	public UserDto userEntityToDto(CrsUser entity) {
		UserDto dto = new UserDto();
		if (Objects.nonNull(entity)) {
			dto.setUserId(entity.getUserId().toString());
			dto.setUserName(entity.getUserName());
			dto.setUserRole(entity.getUserRole().name());
		}
		return dto;
	}

	public CrsUser userDtoToEntity(UserDto dto, UserRoleEnum role) {
		CrsUser entity = new CrsUser();
		if (Objects.nonNull(dto)) {
			entity.setCreatedTime(LocalDateTime.now());
			entity.setPassword(passwordEncoder.encode(dto.getPassword()));
			entity.setUserName(dto.getUserName());
			entity.setUserRole(role);
		}
		return entity;
	}

	public boolean isNumber(String value) {
		return noPattern.matcher(value).matches();
	}

	// returns the mime type of images based on file extension
	public String findMimeType(String fileExtension) {
		return mimeTypeMap.get(fileExtension);
	}

	// Method to convert and return file size in a human-readable format
	public String getReadableFileSize(long sizeInBytes) {
		if (sizeInBytes <= 0)
			return "0 B";

		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int unitIndex = (int) (Math.log10(sizeInBytes) / Math.log10(1024));
		double readableSize = sizeInBytes / Math.pow(1024, unitIndex);

		return String.format("%.2f %s", readableSize, units[unitIndex]);
	}
}
