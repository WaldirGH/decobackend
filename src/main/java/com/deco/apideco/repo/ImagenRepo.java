package com.deco.apideco.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deco.apideco.model.Imagen;

@Repository
public interface ImagenRepo extends JpaRepository<Imagen, Long> {
    void deleteByImgId(String imgId);

    List<Imagen> findByProductoId(Long productoId);
}
