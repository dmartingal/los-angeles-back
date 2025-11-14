package com.clubatletismolosangeles.losangeleswebback.repository;

import com.clubatletismolosangeles.losangeleswebback.model.Noticia;
import com.clubatletismolosangeles.losangeleswebback.model.Categoria; // ajusta si tu clase de categoría tiene otro nombre
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Long> {
    Page<Noticia> findByCategoriaOrderByFechaDesc(Categoria categoria, Pageable pageable);
    Page<Noticia> findAllByOrderByFechaDesc(Pageable pageable);
}
