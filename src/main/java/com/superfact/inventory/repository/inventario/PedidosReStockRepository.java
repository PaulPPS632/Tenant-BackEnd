package com.superfact.inventory.repository.inventario;

import com.superfact.inventory.model.entity.inventario.PedidosReStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidosReStockRepository extends JpaRepository<PedidosReStock, String> {
    List<PedidosReStock> findByTenantId(String tenantId);
}
