package com.eazybyts.boot.exception;

import java.util.List;

import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3414254607954578587L;
	
	private List<String> errors;
	
	public InvalidInputException(List<String> errors)	{
		this.errors = errors;
	}
}
