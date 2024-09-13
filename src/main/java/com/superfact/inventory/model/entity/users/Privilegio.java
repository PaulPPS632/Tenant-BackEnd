package com.superfact.inventory.model.entity.users;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "privilegio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Privilegio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @ManyToMany(mappedBy = "privilegios")
    private List<Rol> roles;
}
