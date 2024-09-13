package com.superfact.inventory.model.dto.inventario;

public record ProductoSeriesRequest(
        String id_producto,
        Long id_Lote,
        String[] sn
) {
    
}