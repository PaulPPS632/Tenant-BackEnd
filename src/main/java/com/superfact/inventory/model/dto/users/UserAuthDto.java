package com.superfact.inventory.model.dto.users;

import lombok.Builder;

@Builder
public record UserAuthDto(
        String id,
        String sub,
        String name,
        String given_name,
        String family_name,
        String picture,
        String email,
        boolean email_verified,
        String locale,
        String password,
        String tenantId,
        String tenantName,
        boolean regist,
        String tiponegocio
) {
}
