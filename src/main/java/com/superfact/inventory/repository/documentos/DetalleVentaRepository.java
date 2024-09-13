package com.superfact.inventory.repository.documentos;

import com.superfact.inventory.model.entity.documentos.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
}
