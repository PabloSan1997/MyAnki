package com.mianki.servicio.servicepart.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 60, unique = true, nullable = false)
    private String username;
    @Column(length = 500, nullable = false)
    private String password;
    @Column(length = 60)
    private String nickname;
    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"id_user", "id_role"})}
    )
    private List<RolesEntity> roles;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<LoginEntity> logins;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MyNotes> notes;

}
