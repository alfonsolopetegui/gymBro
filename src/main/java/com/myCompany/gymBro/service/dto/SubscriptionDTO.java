package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.SubscriptionEntity;
import com.myCompany.gymBro.persistence.enums.NumberOfClasses;
import com.myCompany.gymBro.persistence.enums.PaymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SubscriptionDTO {

    @NotNull(message = "Este campo no puede estar vacío")
    private UUID subscriptionId;

    @NotBlank(message = "Este campo ni puede estar vacío")
    private String subscriptionName;

    @NotEmpty(message = "este campo no puede estar vacío")
    private PaymentType subscriptionType;

    @NotEmpty(message = "este campo no puede estar vacío")
    @Positive(message = "el precio debe ser un valor positivo")
    private Double price;

    @NotNull(message = "Este campo no puede estar vacío")
    private boolean fullMusculation;

    @NotEmpty(message = "este campo no puede estar vacío")
    private NumberOfClasses numberOfClasses;

    @NotNull(message = "Este campo no puede estar vacío")
    private boolean isActive;

    public SubscriptionDTO(SubscriptionEntity subscription) {
        this.fullMusculation = subscription.getFullMusculation();
        this.isActive = subscription.getIsActive();
        this.numberOfClasses = subscription.getNumberOfClasses();
        this.price = subscription.getPrice();
        this.subscriptionId = subscription.getSubscriptionId();
        this.subscriptionName = subscription.getSubscriptionName();
        this.subscriptionType = subscription.getSubscriptionType();
    }
}
