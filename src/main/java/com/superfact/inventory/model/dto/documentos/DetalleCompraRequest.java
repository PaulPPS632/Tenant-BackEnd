package com.superfact.inventory.model.dto.documentos;

import java.util.List;

public record DetalleCompraRequest(
        String id_producto,
        String nombre,
        Long cantidad,
        Double precio_unitario,
        Double precio_total,
        List<String> series
) {
}
