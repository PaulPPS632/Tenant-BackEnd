package com.superfact.inventory.model.entity.correlativos;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "NumeracionComprobante")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NumeracionComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long numeracion;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "tipocomprobante")
    private TipoComprobante tipocomprobante;

    @OneToMany(mappedBy = "numeracioncomprobante", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Correlativo> correlativos;

    @Column(name = "tenant_id")
    private String tenantId;

}
