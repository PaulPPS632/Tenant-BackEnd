package com.superfact.inventory.model.dto.reportes;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record VentaReporteDto(
        LocalDateTime fecha,
        String usuario_id,
        String usuario_name,
        Double monto
) {
}
