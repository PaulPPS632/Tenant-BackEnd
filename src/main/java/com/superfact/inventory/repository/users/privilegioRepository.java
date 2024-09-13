package com.superfact.inventory.repository.users;

import com.superfact.inventory.model.entity.users.Privilegio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface privilegioRepository extends JpaRepository<Privilegio, Long> {
}
