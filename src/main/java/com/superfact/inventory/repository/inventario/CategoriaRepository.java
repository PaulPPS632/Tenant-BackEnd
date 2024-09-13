package com.superfact.inventory.repository.inventario;

import com.superfact.inventory.model.entity.inventario.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    List<Categoria> findByTenantId(String tenandId);
    
} 