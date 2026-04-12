package com.deco.apideco.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deco.apideco.model.Producto;

@Repository
public interface ProductoRepo extends JpaRepository<Producto, Long> {
    
    List<Producto> findByCategoriaId(Long categoriaId);

    Optional<Producto> findByCodigoBarra(String codigoBarra);
}