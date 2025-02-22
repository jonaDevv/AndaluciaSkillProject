package com.jrm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir; // Directorio donde se guardarán los archivos

    // Método para almacenar un archivo
    public String storeFile(MultipartFile file) {
        // Generar un nombre único para el archivo
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir, fileName);

        // Crear el directorio si no existe
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el directorio de archivos: " + e.getMessage());
        }

        // Guardar el archivo
        try {
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error al almacenar el archivo: " + e.getMessage());
        }

        // Retornar la URL o la ruta del archivo guardado
        return path.toString(); // O puedes retornar una URL relativa si usas un servidor web
    }

    // Método para cargar un archivo desde el sistema de archivos
    public byte[] loadFile(String fileName) throws IOException {
        Path path = Paths.get(uploadDir, fileName);
        return Files.readAllBytes(path);
    }
}
