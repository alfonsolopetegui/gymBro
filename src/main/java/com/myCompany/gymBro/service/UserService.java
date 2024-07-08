package com.myCompany.gymBro.service;

import com.myCompany.gymBro.persistence.entity.SubscriptionEntity;
import com.myCompany.gymBro.persistence.entity.UserEntity;
import com.myCompany.gymBro.persistence.enums.UserRole;
import com.myCompany.gymBro.persistence.repository.SubscriptionRepository;
import com.myCompany.gymBro.persistence.repository.UserRepository;
import com.myCompany.gymBro.service.dto.UserCreationDTO;
import com.myCompany.gymBro.service.dto.UserDetailsDTO;
import com.myCompany.gymBro.service.dto.UserSummaryDTO;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public UserService(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public ApiResponse<List<UserDetailsDTO>> getAllUsers() {
        List<UserEntity> users = this.userRepository.findAll();
        List<UserDetailsDTO> userDetails = new ArrayList<>();

        for (UserEntity user:users) {
            UserDetailsDTO newUser = new UserDetailsDTO(user);
            userDetails.add(newUser);
        }

        return new ApiResponse<>("Lista encontrada", 200, userDetails);
    }

    public ApiResponse<UserSummaryDTO> saveUser(UserCreationDTO user) {

        if (this.userRepository.findFirstByEmail(user.getEmail()).isPresent()) {

            return new ApiResponse<>("El usuario ya existe", 400, null);

        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());

        UUID subscriptionId;
        try {
            subscriptionId = UUID.fromString(user.getSubscriptionId());
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>("Formato de UUID inválido para la suscripción", 400, null);
        }

        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        newUser.setSubscription(subscription);

        // Establecer valores predeterminados
        newUser.setLocked(false);
        newUser.setDisabled(false);
        newUser.setUserRole(UserRole.USER);
        newUser.setLastPaymentDate(null);

        UserEntity savedUser = userRepository.save(newUser);
        UserSummaryDTO userSummary = new UserSummaryDTO(savedUser);

        return new ApiResponse<>("Usuario creado correctamente", 200, userSummary);
    }



    public Boolean exists(UUID userId) {
        return this.userRepository.existsById(userId);
    }

    public List<UserSummaryDTO> getAllUsersSummary() {
        List<UserEntity> users = userRepository.getAllByUserRole(UserRole.USER);

        List<UserSummaryDTO> userSummaries = new ArrayList<>();
        for (UserEntity user : users) {
            UserSummaryDTO userSummary = new UserSummaryDTO();
            userSummary.setUserId(user.getUserId());
            userSummary.setUsername(user.getUsername());
            userSummary.setEmail(user.getEmail());

            // Asegúrate de que user.getSubscription() no sea null
            if (user.getSubscription() != null) {
                userSummary.setSubscriptionName(user.getSubscription().getSubscriptionName());
            } else {
                userSummary.setSubscriptionName(null);
            }

            userSummaries.add(userSummary);
        }

        return userSummaries;
    }
}
