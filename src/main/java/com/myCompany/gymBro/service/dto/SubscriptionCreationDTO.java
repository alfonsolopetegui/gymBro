package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.SubscriptionEntity;
import com.myCompany.gymBro.persistence.enums.NumberOfClasses;
import com.myCompany.gymBro.persistence.enums.PaymentType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class SubscriptionCreationDTO {


    @NotEmpty(message = "este campo no puede estar vacío")
    private String subscriptionName;

    @NotNull(message = "este campo no puede ser nulo")
    private PaymentType subscriptionType;

    @Positive(message = "el precio debe ser un valor positivo")
    private Double price;

    @NotNull(message = "este campo no puede ser nulo")
    private boolean fullMusculation;

    @NotNull(message = "este campo no puede estar vacío")
    private NumberOfClasses numberOfClasses;

    @NotNull(message = "este campo no puede ser nulo")
    private boolean isActive;

    public SubscriptionCreationDTO(SubscriptionEntity subscription) {
        this.fullMusculation = subscription.getFullMusculation();
        this.isActive = subscription.getIsActive();
        this.numberOfClasses = subscription.getNumberOfClasses();
        this.price = subscription.getPrice();
        this.subscriptionName = subscription.getSubscriptionName();
        this.subscriptionType = subscription.getSubscriptionType();
    }
}
