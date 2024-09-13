package com.superfact.inventory.model.dto.users;

import lombok.Builder;

@Builder
public record PrivilegioResponse(
        Long id,
        String nombre,
        String descripcion
) {
}
