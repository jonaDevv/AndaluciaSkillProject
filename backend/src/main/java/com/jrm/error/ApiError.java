package com.jrm.error;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Handler;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;



import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;



@Data
@NoArgsConstructor @AllArgsConstructor 
@Builder
public class ApiError {

	@NonNull
	private HttpStatusCode status;
	
	@Builder.Default
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime date = LocalDateTime.now();

	@NonNull
	private String menssage;
	
}
