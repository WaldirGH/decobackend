package com.deco.apideco.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deco.apideco.model.MovimientoInventario;
import com.deco.apideco.model.Enums.TipoMovimiento;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
    List<MovimientoInventario> findByProductoIdOrderByFechaDesc(Long productoId);
    List<MovimientoInventario> findByTipoOrderByFechaDesc(TipoMovimiento tipo);
}
