package com.superfact.inventory.repository.inventario;

import com.superfact.inventory.model.entity.inventario.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    List<Marca> findByTenantId(String tenantId);
    
}
