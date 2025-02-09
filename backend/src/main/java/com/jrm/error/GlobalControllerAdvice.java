package com.jrm.error;



import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jrm.error.user.UserNotFoundException;


@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {
	
	
	
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	
	

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,HttpStatusCode status, WebRequest request) {
		ApiError  apiError = new ApiError(status, ex.getMessage());
		return ResponseEntity.status(status).headers(headers).body(apiError);
	}

	
	

}
