package com.superfact.inventory.repository.inventario;
import com.superfact.inventory.model.entity.inventario.Lote;
import com.superfact.inventory.model.entity.inventario.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoteRepository extends JpaRepository<Lote, Long>{
    Long countByProductoAndTenantId(Producto producto,String tenantId);
}
