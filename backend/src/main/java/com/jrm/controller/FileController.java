package com.jrm.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

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
