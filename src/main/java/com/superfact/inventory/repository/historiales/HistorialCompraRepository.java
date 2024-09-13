package com.superfact.inventory.repository.historiales;

import com.superfact.inventory.model.entity.historiales.HistorialCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialCompraRepository extends JpaRepository<HistorialCompra, String> {
}
