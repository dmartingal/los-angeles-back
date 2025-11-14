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
    private final NoticiaRepository noticiaRepository;

    public NoticiaService(NoticiaRepository repository) {
        this.noticiaRepository = repository;
    }

    @Cacheable(value = "news-pages", key = "T(java.util.Objects).hash(#page, #size)")
    public Page<Noticia> obtenerNoticias(int page, int size) {
        return noticiaRepository.findAllByOrderByFechaDesc(PageRequest.of(page, size));
    }

    public Noticia crearNoticia(Noticia noticia) {
        return noticiaRepository.save(noticia);
    }
}
