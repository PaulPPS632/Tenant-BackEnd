package com.superfact.inventory.model.entity.globales;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="Entidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entidad {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Column(length = 1000)
    private String nombre;

    private String documento;

    @Column(length = 1000)
    private String direccion;

    private String telefono;
    private String email;

    @ManyToOne
    @JoinColumn(name = "id_tipoentidad")
    private TipoEntidad tipoEntidad;

    @Column(name = "tenant_id")
    private String tenantId;
}
