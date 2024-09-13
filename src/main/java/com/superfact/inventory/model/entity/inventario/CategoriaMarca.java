package com.superfact.inventory.model.entity.inventario;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categoriamarca")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaMarca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;

    @OneToMany(mappedBy = "categoriamarca", cascade = CascadeType.ALL)
    private List<Producto> productos;

    @Column(name = "tenant_id")
    private String tenantId;
}
