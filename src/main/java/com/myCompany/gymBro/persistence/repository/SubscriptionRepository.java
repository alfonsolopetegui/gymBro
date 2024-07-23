package com.myCompany.gymBro.persistence.repository;

import com.myCompany.gymBro.persistence.entity.SubscriptionEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface SubscriptionRepository extends ListCrudRepository<SubscriptionEntity, UUID> {

    boolean existsBySubscriptionNameIgnoreCase(String subscriptionName);
}
