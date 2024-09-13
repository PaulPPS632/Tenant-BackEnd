package com.superfact.inventory.repository.documentos;

import com.superfact.inventory.model.entity.documentos.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByTenantId(String tenantId);
}
