package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.SubscriptionEntity;
import com.myCompany.gymBro.persistence.enums.NumberOfClasses;
import com.myCompany.gymBro.persistence.enums.PaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubscriptionDTO {

    private String subscriptionId;
    private String subscriptionName;
    private PaymentType subscriptionType;
    private Double price;
    private boolean fullMusculation;
    private NumberOfClasses numberOfClasses;
    private boolean isActive;

    public SubscriptionDTO(SubscriptionEntity subscription) {
        this.fullMusculation = subscription.getFullMusculation();
        this.isActive = subscription.getIsActive();
        this.numberOfClasses = subscription.getNumberOfClasses();
        this.price = subscription.getPrice();
        this.subscriptionId = String.valueOf(subscription.getSubscriptionId());
        this.subscriptionName = subscription.getSubscriptionName();
        this.subscriptionType = subscription.getSubscriptionType();
    }
}
