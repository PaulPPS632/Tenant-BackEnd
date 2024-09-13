package com.superfact.inventory.model.entity.globales;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TipoCondicion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoCondicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int diascredito;
    private String descripcion;

    @Column(name = "tenant_id")
    private String tenantId;
}
