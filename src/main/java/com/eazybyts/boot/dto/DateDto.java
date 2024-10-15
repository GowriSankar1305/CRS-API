package com.eazybyts.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DateDto {

	private int year;
	private int month;
	private int date;
	
	public DateDto()	{
		
	}
}
