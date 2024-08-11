package com.myCompany.gymBro.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myCompany.gymBro.persistence.enums.NumberOfClasses;
import com.myCompany.gymBro.persistence.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@NoArgsConstructor
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subscription_id", nullable = false, updatable = false)
    private UUID subscriptionId;

    @Column(name = "subscription_name", nullable = false)
    private String subscriptionName;

    @Column(name = "subscription_type")
    @Enumerated(EnumType.STRING)
    private PaymentType subscriptionType;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double price;

    //Gestiona si el usuario tendra acceso a sala de musculación
    @Column(name = "full_musculation", nullable = false)
    private Boolean fullMusculation;

    //Gestiona si el usuario tendra acceso a clases,y a cuantas
    @Column(name = "number_of_classes")
    @Enumerated(EnumType.STRING)
    private NumberOfClasses numberOfClasses;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY)
    private List<UserEntity> users;

    @Override
    public String toString() {
        return "SubscriptionEntity{" +
                "fullMusculation=" + fullMusculation +
                ", subscriptionId=" + subscriptionId +
                ", subscriptionName='" + subscriptionName + '\'' +
                ", subscriptionType=" + subscriptionType +
                ", price=" + price +
                ", numberOfClasses=" + numberOfClasses +
                ", isActive=" + isActive +
                ", users=" + users +
                '}';
    }
}
