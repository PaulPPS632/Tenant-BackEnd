package com.superfact.inventory.repository.inventario;
import com.superfact.inventory.model.entity.inventario.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, String>{


    @Query("SELECT p FROM Producto p WHERE p.nombre LIKE %:keyword% OR p.descripcion LIKE %:keyword%")
    List<Producto> findByNombreOrDescripcionContaining(@Param("keyword") String keyword);

    List<Producto> findByTenantId(String tenandId);

    Optional<Producto> findByIdAndTenantId(String id, String tenantId);
}
