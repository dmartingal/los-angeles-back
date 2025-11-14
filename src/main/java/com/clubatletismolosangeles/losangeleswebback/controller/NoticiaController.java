package com.clubatletismolosangeles.losangeleswebback.controller;

import com.clubatletismolosangeles.losangeleswebback.dto.NoticiaDTO;
import com.clubatletismolosangeles.losangeleswebback.model.Noticia;
import com.clubatletismolosangeles.losangeleswebback.service.LocalFileStorageService;
import com.clubatletismolosangeles.losangeleswebback.service.NoticiaService;
import com.clubatletismolosangeles.losangeleswebback.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/noticias")
@CrossOrigin(origins = "http://localhost:4200")
public class NoticiaController {

    @Autowired
    private NoticiaService noticiaService;

    @Autowired
    private LocalFileStorageService localFileStorageService; // 2. Inyectar el nuevo servicio

    @GetMapping
    public Page<NoticiaDTO> listarNoticias(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        // 1. Obtiene la página con los datos de la BD (con filenames)
        Page<Noticia> paginaNoticias = noticiaService.obtenerNoticias(page, size);

        // 2. Transforma la página para que contenga las URLs completas
        // .map() es una función de Page que nos permite aplicar una
        // transformación a cada elemento de la lista
        return paginaNoticias.map(this::convertirANoticiaDTO);
    }

    /**
     * Método helper que toma una Noticia (con filenames) y
     * la devuelve con las URLs completas construidas.
     */
    private Noticia mapearUrlsCompletas(Noticia noticia) {

        // Construye la URL para la imagen principal
        String urlPrincipal = construirUrlArchivo(noticia.getImagenUrl());
        noticia.setImagenUrl(urlPrincipal);

        // Construye las URLs para las imágenes secundarias
        if (noticia.getImagenesSecundarias() != null) {
            List<String> urlsSecundarias = noticia.getImagenesSecundarias().stream()
                    .map(this::construirUrlArchivo) // Reusa el helper
                    .collect(Collectors.toList());
            noticia.setImagenesSecundarias(urlsSecundarias);
        }

        return noticia;
    }

    /**
     * Helper PRIVADO (movido aquí para ser reutilizable)
     */
    private String construirUrlArchivo(String filename) {
        if (filename == null || filename.isBlank()) {
            return null;
        }

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/uploads/")
                .path(filename)
                .toUriString();
    }

    @PostMapping
    public ResponseEntity<NoticiaDTO> crearNoticia(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("fotoPrincipal") MultipartFile fotoPrincipal,
            @RequestParam("fotosAdicionales") List<MultipartFile> fotosAdicionales) {

        String fotoPrincipalFilename = guardarArchivoUnico(fotoPrincipal);
        List<String> fotosSecundariasFilenames =
                (fotosAdicionales != null)
                        ? fotosAdicionales.stream().map(this::guardarArchivoUnico).toList()
                        : List.of();

        Noticia nuevaNoticia = new Noticia();
        nuevaNoticia.setTitulo(titulo);
        nuevaNoticia.setDescripcion(descripcion);
        nuevaNoticia.setImagenUrl(fotoPrincipalFilename); // Guarda el nombre del archivo
        nuevaNoticia.setImagenesSecundarias(fotosSecundariasFilenames); // Guarda nombres
        nuevaNoticia.setFecha(LocalDate.now());

        Noticia noticiaGuardada = noticiaService.crearNoticia(nuevaNoticia);

        NoticiaDTO dtoRespuesta = convertirANoticiaDTO(noticiaGuardada);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(noticiaGuardada.getId())
                .toUri();

        return ResponseEntity.created(location).body(dtoRespuesta);
    }

    /**
     * Helper que genera un nombre único antes de guardar el archivo.
     */
    private String guardarArchivoUnico(MultipartFile file) {
        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        return localFileStorageService.guardarArchivo(file, uniqueName);
    }

    private NoticiaDTO convertirANoticiaDTO(Noticia noticia) {
        NoticiaDTO dto = new NoticiaDTO();

        // Copia los campos simples
        dto.setId(noticia.getId());
        dto.setTitulo(noticia.getTitulo());
        dto.setDescripcion(noticia.getDescripcion());
        dto.setLinkDetalle(noticia.getLinkDetalle());
        dto.setFecha(noticia.getFecha());
        dto.setCategoria(noticia.getCategoria());

        // Construye las URLs completas al momento de crear el DTO
        dto.setImagenUrl(construirUrlArchivo(noticia.getImagenUrl()));

        if (noticia.getImagenesSecundarias() != null) {
            List<String> urlsSecundarias = noticia.getImagenesSecundarias().stream()
                    .map(this::construirUrlArchivo) // Reusa el helper de URL
                    .collect(Collectors.toList());
            dto.setImagenesSecundarias(urlsSecundarias);
        }

        return dto;
    }
}