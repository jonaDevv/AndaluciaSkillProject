package com.jrm.error;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Handler;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;



import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ApiError {

	@NonNull
	private HttpStatusCode status;
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime date = LocalDateTime.now();
	@NonNull
	private String menssage;
	
}
