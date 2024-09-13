package com.superfact.inventory.model.dto.documentos;

import lombok.Builder;

import java.util.List;

@Builder
public record DetalleVentaRequest(
        String id_producto,
        String nombre,
        Long cantidad,
        List<String> series,
        Double precio_unitario,
        Double precio_total
) {
}
