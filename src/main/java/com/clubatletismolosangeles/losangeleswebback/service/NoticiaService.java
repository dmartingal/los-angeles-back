package com.clubatletismolosangeles.losangeleswebback.service;
import com.clubatletismolosangeles.losangeleswebback.model.Noticia;
import com.clubatletismolosangeles.losangeleswebback.repository.NoticiaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    public Page<Noticia> obtenerNoticias(int page, int size) {
        return noticiaRepository.findAllByOrderByFechaDesc(PageRequest.of(page, size));
    }

    public Noticia crearNoticia(Noticia noticia) {
        return noticiaRepository.save(noticia);
    }

    public Noticia buscarPorId(Long id) {
        return noticiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Noticia no encontrada con id " + id));
    }

    public Noticia actualizarNoticia(Noticia noticia) {
        return noticiaRepository.save(noticia);
    }

    public void eliminarNoticia(Long id) {
        if (!noticiaRepository.existsById(id)) {
            throw new RuntimeException("La noticia con ID " + id + " no existe");
        }
        noticiaRepository.deleteById(id);
    }
}