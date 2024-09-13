package com.superfact.inventory.model.entity.historiales;

import com.superfact.inventory.model.entity.documentos.Venta;
import com.superfact.inventory.model.entity.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "HistorialVenta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDateTime fecha;
    private String accion;
    private String detalles;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;
}
