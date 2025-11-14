package com.clubatletismolosangeles.losangeleswebback.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * Guarda un archivo y devuelve su identificador único (ej: "12345.jpg").
     * Lanza una excepción si falla.
     */
    public String guardarArchivo(MultipartFile file, String customFilename);

    /**
     * Carga un archivo basado en su identificador (filename).
     */
    public Resource cargarComoRecurso(String filename);

    /**
     * (Opcional pero recomendado) Borra un archivo.
     */
    // public void borrarArchivo(String filename);

    /**
     * (Opcional) Usado para inicializar el almacenamiento (ej: crear la carpeta).
     */
    // public void init();
}