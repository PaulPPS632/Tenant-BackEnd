package com.superfact.inventory.model.dto.users;

import lombok.Builder;

@Builder
public record UserResponse(
        String id,
        String sub,
        String name,
        String given_name,
        String family_name,
        String picture,
        String email,
        boolean email_verified,
        String locale,
        String tenantId,
        String tenantName,
        boolean regist,
        String tiponegocio,
        RolResponse rol
) {
}
