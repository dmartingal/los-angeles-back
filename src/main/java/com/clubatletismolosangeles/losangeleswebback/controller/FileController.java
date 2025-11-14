package com.clubatletismolosangeles.losangeleswebback.controller;

import com.clubatletismolosangeles.losangeleswebback.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uploads") // O la URL que prefieras para tus archivos
@CrossOrigin(origins = "http://localhost:4200")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> servirArchivo(@PathVariable String filename) {

        Resource file = fileStorageService.cargarComoRecurso(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}