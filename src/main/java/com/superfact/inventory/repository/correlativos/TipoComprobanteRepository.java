package com.superfact.inventory.repository.correlativos;

import com.superfact.inventory.model.entity.correlativos.TipoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoComprobanteRepository extends JpaRepository<TipoComprobante, Long> {
    Optional<TipoComprobante> findByPrefijo(String prefijo);
}
