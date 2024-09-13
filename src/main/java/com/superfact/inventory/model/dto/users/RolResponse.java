package com.superfact.inventory.model.dto.users;

import com.superfact.inventory.model.entity.users.Privilegio;
import lombok.Builder;

import java.util.List;

@Builder
public record RolResponse(
        Long id,
        String nombre,
        String descripcion,
        List<PrivilegioResponse> privilegios
) {
}
