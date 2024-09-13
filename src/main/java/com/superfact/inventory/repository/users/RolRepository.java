package com.superfact.inventory.repository.users;

import com.superfact.inventory.model.entity.users.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
}
