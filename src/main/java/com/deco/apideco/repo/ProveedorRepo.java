package com.deco.apideco.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deco.apideco.model.Proveedor;

@Repository
public interface ProveedorRepo extends JpaRepository<Proveedor, Long> {
}
