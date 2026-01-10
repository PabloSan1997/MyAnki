package com.mianki.servicio.servicepart.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mianki.servicio.servicepart.models.enums.NoteLevel;
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
@Table(name = "my_notes")
public class MyNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 250, nullable = false)
    private String front;
    @Column(length = 250)
    private String back;
    @Column(length = 250, nullable = false)
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime nextreview;
    private Double days;
    private String previewtimegood;
    private String previewtimehard;
    @Enumerated(EnumType.STRING)
    private NoteLevel noteLevel;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private UserEntity user;

    @PrePersist
    public void prepersist(){
        LocalDateTime dateTime = LocalDateTime.now();
        createdAt = dateTime;
        nextreview = dateTime;
        days = 0.0;
        previewtimegood = "1 min";
        previewtimehard = "1 min";
        noteLevel = NoteLevel.INITIAL;
    }


}
