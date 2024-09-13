package com.superfact.inventory.repository.inventario;

import com.superfact.inventory.model.entity.inventario.ProductoSerie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoSerieRepository extends JpaRepository<ProductoSerie, String> {
    Optional<ProductoSerie> findBySn(String sn);
    Optional<ProductoSerie> findBySnAndTenantId(String sn, String tenantId);
}
