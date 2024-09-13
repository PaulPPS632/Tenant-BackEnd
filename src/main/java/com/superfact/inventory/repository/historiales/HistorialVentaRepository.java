package com.superfact.inventory.repository.historiales;

import com.superfact.inventory.model.entity.historiales.HistorialVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialVentaRepository extends JpaRepository<HistorialVenta, String> {
}
