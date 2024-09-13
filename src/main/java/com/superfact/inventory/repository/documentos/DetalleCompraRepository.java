package com.superfact.inventory.repository.documentos;

import com.superfact.inventory.model.entity.documentos.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, String> {
}
