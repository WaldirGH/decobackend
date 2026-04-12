package com.deco.apideco.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deco.apideco.model.Categoria;

@Repository
public interface CategoriaRepo extends JpaRepository<Categoria, Long> {
}
