package com.superfact.inventory.model.entity.correlativos;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "Correlativo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Correlativo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long numero;

    @ManyToOne
    @JoinColumn(name = "id_numeracioncomprobante")
    private NumeracionComprobante numeracioncomprobante;

    @Column(name = "tenant_id")
    private String tenantId;
}
