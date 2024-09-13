package com.superfact.inventory.model.entity.users;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Rol")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rol_privilegio",
            joinColumns = @JoinColumn(name = "rol_id"),
            inverseJoinColumns = @JoinColumn(name = "privilegio_id")
    )
    private List<Privilegio> privilegios;
}
