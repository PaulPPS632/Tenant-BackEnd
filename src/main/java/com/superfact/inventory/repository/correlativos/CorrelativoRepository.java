package com.superfact.inventory.repository.correlativos;

import com.superfact.inventory.model.entity.correlativos.Correlativo;
import com.superfact.inventory.model.entity.correlativos.NumeracionComprobante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CorrelativoRepository extends JpaRepository<Correlativo, UUID> {
    long countByNumeracioncomprobante(NumeracionComprobante numeracioncomprobante);
}
