package com.myCompany.gymBro.service;

import com.myCompany.gymBro.exception.SubscriptionNotFoundException;
import com.myCompany.gymBro.exception.UserNotFoundException;
import com.myCompany.gymBro.persistence.entity.SubscriptionEntity;
import com.myCompany.gymBro.persistence.entity.UserEntity;
import com.myCompany.gymBro.persistence.enums.UserRole;
import com.myCompany.gymBro.persistence.repository.SubscriptionRepository;
import com.myCompany.gymBro.persistence.repository.UserRepository;
import com.myCompany.gymBro.service.dto.UserCreationDTO;
import com.myCompany.gymBro.service.dto.UserDetailsDTO;
import com.myCompany.gymBro.service.dto.UserSummaryDTO;
import com.myCompany.gymBro.service.dto.UserUpdateDTO;
import com.myCompany.gymBro.utils.ValidationUtils;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

        for (UserEntity user : users) {
            UserDetailsDTO newUser = new UserDetailsDTO(user);
            userDetails.add(newUser);
        }

        return new ApiResponse<>("Lista encontrada", 200, userDetails);
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

    public ApiResponse<UserSummaryDTO> getUser(String userId) {

        System.out.println("Entro");
        if (!ValidationUtils.isValidUUID(userId)) {
            throw new IllegalArgumentException("Formato de UUID inválido");
        }
        UUID id = UUID.fromString(userId);

        System.out.println(userId);

        UserEntity user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));

        System.out.println("paso aqui");

        UserSummaryDTO userSummary = new UserSummaryDTO();

        // Asegúrate de que user.getSubscription() no sea null
        if (user.getSubscription() != null) {
            userSummary.setSubscriptionName(user.getSubscription().getSubscriptionName());
        } else {
            userSummary.setSubscriptionName(null);
        }

        userSummary.setUsername(user.getUsername());
        userSummary.setUserId(user.getUserId());
        userSummary.setEmail(user.getEmail());

        return new ApiResponse<>("Usuario encontrado", 200, userSummary);
    }

    public ApiResponse<UserSummaryDTO> saveUser(UserCreationDTO user) {

        //Valido si el usuario ya existe
        if (this.userRepository.findFirstByEmail(user.getEmail()).isPresent()) {
            return new ApiResponse<>("El email ya existe", 400, null);
        }

        //Valido si el id del subscription es un UUID valido
        if (!ValidationUtils.isValidUUID(user.getSubscriptionId())) {
            throw new IllegalArgumentException("Formato de UUID inválido para la suscripción");
        }

        UUID subscriptionId = UUID.fromString(user.getSubscriptionId());

        UserEntity newUser = new UserEntity();

        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());

        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException("Suscripción no encontrada"));

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

    public ApiResponse<UserDetailsDTO> updateUser(UserUpdateDTO user) {

        if (!ValidationUtils.isValidUUID(user.getUserId())) {
            throw new IllegalArgumentException("ID de usuario en formato UUID inválido");
        }
        UUID userId = UUID.fromString(user.getUserId());
        // Buscar el usuario existente
        UserEntity existingUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        // Actualizar los campos del usuario existente solo si se proporcionan en el DTO
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            existingUser.setEmail(user.getEmail());
        }

        if (user.getUserRole() != null) {
            existingUser.setUserRole(user.getUserRole());
        }
        if (user.getSubscriptionId() != null && !user.getSubscriptionId().isEmpty()) {
            UUID subscriptionId = UUID.fromString(user.getSubscriptionId());
            existingUser.setSubscription(this.subscriptionRepository.findById(subscriptionId)
                    .orElseThrow(() -> new SubscriptionNotFoundException("No encontramos la subscripción")));
        }

        if (user.getLocked() != existingUser.getLocked()) {
            existingUser.setLocked(user.getLocked());
        }
        if (user.getDisabled() != existingUser.getDisabled()) {
            existingUser.setDisabled(user.getDisabled());
        }

        // Guardar el usuario actualizado
        UserEntity updatedUser = userRepository.save(existingUser);

        // Convertir la entidad actualizada a DTO
        UserDetailsDTO updatedUserDTO = new UserDetailsDTO();
        updatedUserDTO.setUserId(updatedUser.getUserId());
        updatedUserDTO.setUsername(updatedUser.getUsername());
        updatedUserDTO.setEmail(updatedUser.getEmail());
        updatedUserDTO.setUserRole(updatedUser.getUserRole());
        updatedUserDTO.setLocked(updatedUser.getLocked());
        updatedUserDTO.setDisabled(updatedUser.getDisabled());

        if (updatedUser.getSubscription() != null) {
            updatedUserDTO.setSubscriptionName(updatedUser.getSubscription().getSubscriptionName());
            updatedUserDTO.setSubscriptionPrice(updatedUser.getSubscription().getPrice());
        }


        // Devolver la respuesta
        return new ApiResponse<>("Usuario actulizado correctamente", 200, updatedUserDTO);
    }

    public ApiResponse<Void> deleteUser(String userId) {

        if (!ValidationUtils.isValidUUID(userId)) {
            throw new IllegalArgumentException("Formato de UUID inválido");
        }

        UUID id = UUID.fromString(userId);

        if (!exists(id)) {
            throw new UserNotFoundException("El usuario que buscas no existe");
        }

        try {
            this.userRepository.deleteById(id);
            return new ApiResponse<>("Usuario borrado exitosamente", 200, null);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ApiResponse<>("Error al borrar el usuario", 500, null);
        }

    }

    public Boolean exists(UUID userId) {
        return this.userRepository.existsById(userId);
    }


}
