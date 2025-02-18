package com.jrm.error;



import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jrm.error.evaluacion.EvaluacionNotFoundException;
import com.jrm.error.evaluacionitem.EvaluacionItemNotFoundException;
import com.jrm.error.item.ItemNotFoundException;
import com.jrm.error.participant.ParticipantNotFoundException;
import com.jrm.error.prueba.PruebaNotFoundException;
import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.error.user.UserNotFoundException;
import com.jrm.error.user.UsernameExistsException;

import jakarta.validation.ConstraintViolationException;


@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {
	
	
	
	
	@ExceptionHandler({UserNotFoundException.class, UsernameExistsException.class})
	public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler(SpecialtyNotFoundException.class)
    public ResponseEntity<ApiError> handleSpecialtyNotFound(SpecialtyNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage());
            
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

	@ExceptionHandler(ParticipantNotFoundException.class)
    public ResponseEntity<ApiError> handleSpecialtyNotFound(ParticipantNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage());
            
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

	
	@ExceptionHandler(PruebaNotFoundException.class)
    public ResponseEntity<ApiError> handleSpecialtyNotFound(PruebaNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage());
            
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

	
	@ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiError> handleSpecialtyNotFound(ItemNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage());
            
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(EvaluacionNotFoundException.class)
    public ResponseEntity<ApiError> handleEvaluacionNotFound(EvaluacionNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage());
            
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }


    @ExceptionHandler(  EvaluacionItemNotFoundException.class)
    public ResponseEntity<ApiError> handleEvaluacionItemNotFound(EvaluacionItemNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage());
            
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
    

   
	
	@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleSpecialtyNotFound(ConstraintViolationException  ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, LocalDateTime.now(),ex.getConstraintViolations().stream()
		.map(violation -> violation.getMessageTemplate()) // Obtener solo el messageTemplate
		.findFirst()
		.orElse("Se ha producido un error inesperado, consulta el log para más información"));
            
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
	
	

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,HttpStatusCode status, WebRequest request) {
		ApiError  apiError = new ApiError(status, LocalDateTime.now(), ex.getMessage());
		return ResponseEntity.status(status).headers(headers).body(apiError);
	}

    
}

	
	


