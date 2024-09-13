package com.superfact.inventory.model.entity.inventario;

import com.superfact.inventory.model.entity.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PedidosReStock")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidosReStock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User usuario;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private String estado;

    @Column(length = 1000)
    private String nota;

    private Long cantidad;
    private String tenantId;

}
