package com.myCompany.gymBro.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "class_type")
@Getter
@Setter
@NoArgsConstructor
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "class_id", nullable = false, updatable = false)
    private UUID classId;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "class_description", columnDefinition = "TEXT")
    private String classDescription;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive;

}
