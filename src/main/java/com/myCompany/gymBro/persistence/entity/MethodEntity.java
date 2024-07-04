package com.myCompany.gymBro.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "payment_method")
@Getter
@Setter
@NoArgsConstructor
public class MethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "method_id", nullable = false, updatable = false)
    private UUID methodId;

    @Column(name = "method_name", nullable = false)
    private String methodName;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive;

    @OneToMany(mappedBy = "method", fetch = FetchType.LAZY)
    private List<PaymentEntity> payments;
}
