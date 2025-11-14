package com.clubatletismolosangeles.losangeleswebback.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "noticias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Noticia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @Column(length = 2000)
    private String descripcion;
    private String imagenUrl;
    @ElementCollection
    @CollectionTable(name = "noticia_imagenes", joinColumns = @JoinColumn(name = "noticia_id"))
    @Column(name = "url")
    private List<String> imagenesSecundarias;
    private String linkDetalle;
    private LocalDate fecha;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
}
