package com.superfact.inventory.model.entity.users;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String sub;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String email;
    private boolean email_verified;
    private String locale;
    private String password;
    @Column(name = "tenant_id")
    private String tenantId;
    private String tenantName;
    private boolean Regist;
    private String tiponegocio;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

}
