package com.superfact.inventory.model.entity.correlativos;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "TipoComprobante")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prefijo;
    private String descripcion;

    @OneToMany(mappedBy = "tipocomprobante", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<NumeracionComprobante> numeraciones;

    @Column(name = "tenant_id")
    private String tenantId;
}
