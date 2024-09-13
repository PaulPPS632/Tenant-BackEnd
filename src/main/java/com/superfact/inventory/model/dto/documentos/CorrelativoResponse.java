package com.superfact.inventory.model.dto.documentos;

import lombok.Builder;

@Builder
public record CorrelativoResponse(
        String prefijo,
        Long numeracion,
        Long correlativo,
        String documento
) {
}
