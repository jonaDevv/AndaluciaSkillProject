package com.jrm.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jrm.error.ApiError;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/files")
@SecurityRequirement(name = "bearerAuth") 
@Tag(name = "Gestión de Archivos", description = "Operaciones para la gestión de archivos almacenados")
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Operation(summary = "Descargar archivo", 
               description = "Obtener un archivo del sistema de almacenamiento por su nombre")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Archivo descargado exitosamente",
                   content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, 
                            schema = @Schema(type = "string", format = "binary"))),
        @ApiResponse(responseCode = "400", description = "Nombre de archivo inválido",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Archivo no encontrado",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            // Decodificar el nombre del archivo en caso de que contenga caracteres especiales
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);

            // Construir la ruta completa del archivo
            Path filePath = Paths.get(uploadDir).resolve(decodedFileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                // Determinar el tipo de contenido
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                // Devolver el archivo con los encabezados adecuados
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // Archivo no encontrado
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            // Error en la formación de la URL
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            // Error al acceder al archivo
            return ResponseEntity.status(500).body(null);
        }
    }
}
