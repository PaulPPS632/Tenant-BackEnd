package com.superfact.inventory.repository.tenant;

import com.superfact.inventory.model.entity.Tenant.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, String> {
}
