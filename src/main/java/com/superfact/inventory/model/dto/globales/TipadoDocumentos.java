package com.superfact.inventory.model.dto.globales;

import com.superfact.inventory.model.dto.documentos.TipoComprobanteResponse;
import com.superfact.inventory.model.entity.correlativos.TipoComprobante;
import com.superfact.inventory.model.entity.globales.TipoCondicion;
import com.superfact.inventory.model.entity.globales.TipoMoneda;
import com.superfact.inventory.model.entity.globales.TipoPago;

import java.util.List;

public record TipadoDocumentos(
        List<TipoComprobanteResponse> tipocomprobantes,
        List<TipoCondicion> tipocondiciones,
        List<TipoPago> tipopagos,
        List<TipoMoneda> tipomonedas
) {
}
