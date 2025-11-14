package com.clubatletismolosangeles.losangeleswebback.dto;

import com.clubatletismolosangeles.losangeleswebback.model.Categoria;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para transferir la información de Noticia al frontend.
 * Contendrá las URLs completas de las imágenes.
 */
@Getter
@Setter
@NoArgsConstructor
public class NoticiaDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String imagenUrl;
    private List<String> imagenesSecundarias;
    private String linkDetalle;
    private LocalDate fecha;
    private Categoria categoria;

}