package com.jrm.error;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Handler;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import io.swagger.v3.oas.annotations.media.Schema;
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

	@Schema(description = "Código de estado HTTP", example = "404")
	@NonNull
	private HttpStatusCode status;
	
	@Schema(description = "Timestamp del error", example = "2023-10-05T12:34:56.789Z")
	@Builder.Default
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime date = LocalDateTime.now();

	@Schema(description = "Mensaje descriptivo", example = "Recurso no encontrado")
	@NonNull
	private String menssage;
	
}
