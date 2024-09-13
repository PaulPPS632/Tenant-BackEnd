package com.superfact.inventory.model.dto.inventario;

import com.superfact.inventory.model.dto.users.UserResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PedidosReStockResponse(
        String id,
        UserResponse usuario,
        LocalDateTime fecha,
        ProductoResponse producto,
        String estado,
        Long cantidad,
        String nota,
        String tenantId
) {
}
