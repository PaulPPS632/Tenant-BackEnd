package com.superfact.inventory.model.entity.users;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LogicaNegocioUser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogicaNegocioUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String usuario;

    @Column(length = 4000)
    private String logica;

    private Double metaventas;
}
