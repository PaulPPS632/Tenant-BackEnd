package com.superfact.inventory.model.entity.documentos;

import com.superfact.inventory.model.entity.correlativos.Correlativo;
import com.superfact.inventory.model.entity.globales.Entidad;
import com.superfact.inventory.model.entity.globales.TipoCondicion;
import com.superfact.inventory.model.entity.globales.TipoMoneda;
import com.superfact.inventory.model.entity.globales.TipoPago;
import com.superfact.inventory.model.entity.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Venta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "id_correlativo")
    private Correlativo correlativo;

    @ManyToOne
    @JoinColumn(name = "id_entidad_cliente")
    private Entidad entidad_cliente;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_tipocondicion")
    private TipoCondicion tipocondicion;

    @ManyToOne
    @JoinColumn(name = "id_tipopago")
    private TipoPago tipopago;

    private LocalDateTime fecha_emision;
    private LocalDateTime fecha_vencimiento;

    @Column(length = 2000)
    private String nota;

    private Double gravada;
    private Double impuesto;
    private Double total;
    private LocalDateTime fechapago;
    private String formapago;
    private String url_pdf;

    @ManyToOne
    @JoinColumn(name = "id_tipomoneda")
    private TipoMoneda tipomoneda;
    private Double tipo_cambio;

    @OneToMany(mappedBy = "venta")
    private List<DetalleVenta> detalleVenta;

    @Column(name = "tenant_id")
    private String tenantId;
}
