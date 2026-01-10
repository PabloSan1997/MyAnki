package com.mianki.servicio.servicepart.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "logins")
public class LoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean state;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @PrePersist
    public void prepersist(){
        var nowdate = LocalDateTime.now();
        createdAt = nowdate;
        updatedAt = nowdate;
        state = true;
    }
    @PreUpdate
    public void preupdate(){
        updatedAt = LocalDateTime.now();
    }
}
