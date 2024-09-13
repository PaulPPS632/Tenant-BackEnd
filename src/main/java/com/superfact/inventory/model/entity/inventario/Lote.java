package com.superfact.inventory.model.entity.inventario;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lote")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private LocalDateTime fecha;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @OneToMany(mappedBy = "lote", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductoSerie> productoserie;

    @Override
    public String toString() {
        return "Lote{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fecha=" + fecha +
                '}';
    }

    @Column(name = "tenant_id")
    private String tenantId;
}

