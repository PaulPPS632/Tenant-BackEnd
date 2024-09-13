package com.superfact.inventory.repository.documentos;

import com.superfact.inventory.model.entity.documentos.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CompraRepository extends JpaRepository<Compra, UUID> {
    List<Compra> findByTenantId(String tenantId);
}
