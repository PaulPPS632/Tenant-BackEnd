package com.superfact.inventory.model.entity.documentos;

import com.superfact.inventory.model.entity.inventario.Producto;
import com.superfact.inventory.model.entity.inventario.ProductoSerie;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DetalleCompra")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_compra")
    private Compra compra;

    private String sn;

    @ManyToOne
    @JoinColumn(name = "id_productoserie")
    private ProductoSerie productoserie;

    private Double precio_neto;

    @Column(name = "tenant_id")
    private String tenantId;
}
