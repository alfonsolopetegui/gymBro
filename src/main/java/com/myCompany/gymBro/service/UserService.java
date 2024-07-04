package com.myCompany.gymBro.service;

import com.myCompany.gymBro.persistence.entity.UserEntity;
import com.myCompany.gymBro.persistence.repository.UserRepository;
import com.myCompany.gymBro.service.dto.UserSummaryDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers() {
        return (List<UserEntity>) this.userRepository.findAll();
    }

    public List<UserSummaryDTO> getAllUsersSummary() {
        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();

        List<UserSummaryDTO> userSummaries = new ArrayList<>();
        for (UserEntity user : users) {
            UserSummaryDTO userSummary = new UserSummaryDTO();
            userSummary.setUserId(user.getUserId());
            userSummary.setUsername(user.getUsername());
            userSummary.setEmail(user.getEmail());
            userSummary.setUserRole(String.valueOf(user.getUserRole()));
            userSummaries.add(userSummary);
        }

        return userSummaries;
    }
}
