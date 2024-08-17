package com.myCompany.gymBro.service;

import com.myCompany.gymBro.exception.SubscriptionNotFoundException;
import com.myCompany.gymBro.persistence.entity.SubscriptionEntity;
import com.myCompany.gymBro.persistence.enums.NumberOfClasses;
import com.myCompany.gymBro.persistence.enums.PaymentType;
import com.myCompany.gymBro.persistence.repository.SubscriptionRepository;
import com.myCompany.gymBro.service.dto.SubscriptionCreationDTO;
import com.myCompany.gymBro.service.dto.SubscriptionDTO;
import com.myCompany.gymBro.utils.ValidationUtils;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public ApiResponse<List<SubscriptionDTO>> getAllSubscriptions() {

        try {

            List<SubscriptionEntity> subscriptionEntityList = this.subscriptionRepository.findAll();
            if (subscriptionEntityList.isEmpty()) {
                return new ApiResponse<>("No hay subscripciones para mostrar", 404, null);
            }

            List<SubscriptionDTO> subscriptionDTOList = subscriptionEntityList.stream()
                    .map(SubscriptionDTO::new)
                    .collect(Collectors.toList());

            return new ApiResponse<>("Se muestra la lista de subscriptions", 200, subscriptionDTOList);

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>("Error al obtener las subscriptions", 500, null);

        }

    }

    public ApiResponse<SubscriptionDTO> saveSubscription(SubscriptionCreationDTO subscriptionCreationDTO) {

        if (this.subscriptionRepository.existsBySubscriptionNameIgnoreCase(subscriptionCreationDTO.getSubscriptionName())) {
            return new ApiResponse<>("El nombre de subscription ya está en uso", 404, null);
        }


        if (!ValidationUtils.isValidNumberOfClasses(subscriptionCreationDTO.getNumberOfClasses())) {
            return new ApiResponse<>("numberOfClasses no es un valor válido", 404, null);
        }

        try {
            PaymentType.valueOf(String.valueOf(subscriptionCreationDTO.getSubscriptionType()));
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>("El valor subscriptionType no es válido", 404, null);
        }

        SubscriptionEntity subscription = new SubscriptionEntity();

        subscription.setSubscriptionName(subscriptionCreationDTO.getSubscriptionName());
        subscription.setFullMusculation(subscriptionCreationDTO.isFullMusculation());
        subscription.setIsActive(subscriptionCreationDTO.isActive());
        subscription.setNumberOfClasses(subscriptionCreationDTO.getNumberOfClasses());
        subscription.setPrice(subscriptionCreationDTO.getPrice());
        subscription.setSubscriptionType(subscriptionCreationDTO.getSubscriptionType());
        subscription.setUsers(new ArrayList<>());

        try {
            SubscriptionEntity savedSubscription = this.subscriptionRepository.save(subscription);
            SubscriptionDTO subscriptionResponse = new SubscriptionDTO(savedSubscription);
            return new ApiResponse<>("Subscription creada exitosamente", 200, subscriptionResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>("Error al guardar los datos", 500, null);
        }

    }

    public ApiResponse<Void> deleteSubscription(String subscriptionId) {

        if (!ValidationUtils.isValidUUID(subscriptionId)) {
            return new ApiResponse<>("El Id no es un UUID válido", 404, null);
        }

        UUID id = UUID.fromString(subscriptionId);

        if (!this.subscriptionRepository.existsById(id)) {
            throw new SubscriptionNotFoundException("La subscription que buscas no existe");
        }

        try {
            this.subscriptionRepository.deleteById(id);
            return new ApiResponse<>("Subscription eliminada con éxito", 200, null);
        }catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>("Error al eliminar subscription", 500, null);
        }
    }

    public ApiResponse<SubscriptionDTO> updateSubscription(SubscriptionDTO subscriptionDTO) {

        if (!ValidationUtils.isValidUUID(String.valueOf(subscriptionDTO.getSubscriptionId()))) {
            return new ApiResponse<>("El id no es un UUID válido", 404, null);
        }

        UUID subscriptionId = UUID.fromString(String.valueOf(subscriptionDTO.getSubscriptionId()));

        SubscriptionEntity existingSubscription = this.subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException("La subscription que buscas no existe"));

        existingSubscription.setSubscriptionName(subscriptionDTO.getSubscriptionName());
        existingSubscription.setSubscriptionType(subscriptionDTO.getSubscriptionType());
        existingSubscription.setPrice(subscriptionDTO.getPrice());
        existingSubscription.setNumberOfClasses(subscriptionDTO.getNumberOfClasses());
        existingSubscription.setIsActive(subscriptionDTO.isActive());
        existingSubscription.setFullMusculation(subscriptionDTO.isFullMusculation());

        try {
            SubscriptionEntity updatedSubscription = this.subscriptionRepository.save(existingSubscription);
            SubscriptionDTO subscriptionResponse = new SubscriptionDTO(updatedSubscription);
            return new ApiResponse<>("Subscription modificada con éxito", 200, subscriptionResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>("Error al modificar la subscription", 500, null);
        }
    }

}
