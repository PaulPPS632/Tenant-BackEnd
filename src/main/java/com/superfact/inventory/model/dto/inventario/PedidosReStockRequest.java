package com.superfact.inventory.model.dto.inventario;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PedidosReStockRequest(
        String id,
        String id_usuario,
        LocalDateTime fecha,
        String id_producto,
        String estado,
        Long cantidad,
        String nota,
        String tenantId
) {
}
