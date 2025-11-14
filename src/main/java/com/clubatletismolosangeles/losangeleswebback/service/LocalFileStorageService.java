package com.clubatletismolosangeles.losangeleswebback.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path rootLocation;

    public LocalFileStorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el directorio de almacenamiento", e);
        }
    }

    @Override
    public String guardarArchivo(MultipartFile file, String customFilename) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Error al guardar un archivo vacío.");
            }

            Path destinationFile = this.rootLocation.resolve(Paths.get(customFilename))
                    .normalize()
                    .toAbsolutePath();

            Files.copy(file.getInputStream(), destinationFile);
            return customFilename;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo.", e);
        }
    }

    @Override
    public Resource cargarComoRecurso(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer el archivo: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}