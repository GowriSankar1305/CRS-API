package com.eazybyts.boot.exception;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eazybyts.boot.dto.ApiResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CrsExceptionHandler {

	@ExceptionHandler(value = InvalidInputException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleInvalidInputException(InvalidInputException e) {
		log.error("input error {}", e);
		return new ResponseEntity<>(new ApiResponseDto(e.getErrors()), HttpStatus.BAD_REQUEST);
	}
	
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = RuntimeException.class)
	public ApiResponseDto handleRuntimeException(RuntimeException re) {
		log.error("runtime error {}", re);
		return new ApiResponseDto(List.of("Unable to process your request!"));
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public ApiResponseDto handleRuntimeException(Exception e) {
		log.error("system error {}", e);
		return new ApiResponseDto(List.of("System error has occurred!"));
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ApiResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException me) {
		log.error("validation failed-----> {}", me);
		return new ApiResponseDto(me.getFieldErrors().stream()
				.map(mapToErrorMsg).collect(Collectors.toList()));
	}

	private Function<FieldError, String> mapToErrorMsg = (error) -> {
		return error.getField() + " " + error.getDefaultMessage();
	};
}
