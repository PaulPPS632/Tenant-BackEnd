package com.superfact.inventory.repository.correlativos;

import com.superfact.inventory.model.entity.correlativos.NumeracionComprobante;
import com.superfact.inventory.model.entity.correlativos.TipoComprobante;
import com.superfact.inventory.model.entity.inventario.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NumeracionComprobanteRepository extends JpaRepository<NumeracionComprobante, Long> {

    Optional<NumeracionComprobante> findByTipocomprobanteAndNumeracionAndTenantId(TipoComprobante tipocomprobante, Long numeracion, String tenantId);

    //@Query("SELECT nc FROM NumeracionComprobante nc WHERE nc.tipocomprobante.id = :id_tipocomprobante AND nc.numeracion = :numeracion")
    //Optional<NumeracionComprobante> buscar(@Param("numeracion") Long numeracion, @Param("id_tipocomprobante") Long id_tipocomprobante);

    //@Query("SELECT nc FROM NumeracionComprobante nc WHERE nc.tipocomprobante = :id_tipocomprobante AND nc.numeracion = :numeracion")
    //Optional<NumeracionComprobante> findByTipocomprobanteAndNumeracion(@Param("numeracion") Long numeracion, @Param("id_tipocomprobante") Long id_tipocomprobante);

}
