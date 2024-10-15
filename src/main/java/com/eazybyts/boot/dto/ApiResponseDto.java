package com.eazybyts.boot.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseDto {

	public ApiResponseDto()	{
		
	}
	
	public ApiResponseDto(String message,int code)	{
		this.message = message;
		this.statusCode = code;
	}
	
	public ApiResponseDto(List<String> errors)	{
		this.errors = errors;
	}
	
	private String message;
	private int statusCode;
	private List<String> errors;
	private String token;
	private String imageUrl;
	private String imageDownloadUrl;
	private String imageId;
}
