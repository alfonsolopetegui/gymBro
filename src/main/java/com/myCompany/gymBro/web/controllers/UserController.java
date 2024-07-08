package com.myCompany.gymBro.web.controllers;

import com.myCompany.gymBro.persistence.entity.UserEntity;
import com.myCompany.gymBro.service.UserService;
import com.myCompany.gymBro.service.dto.UserCreationDTO;
import com.myCompany.gymBro.service.dto.UserDetailsDTO;
import com.myCompany.gymBro.service.dto.UserSummaryDTO;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDetailsDTO>>> getAllUsers() {
        ApiResponse<List<UserDetailsDTO>> response = this.userService.getAllUsers();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserSummaryDTO>> saveUser(@RequestBody UserCreationDTO user) {
        ApiResponse<UserSummaryDTO> response = this.userService.saveUser(user);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/summary")
    public ResponseEntity<List<UserSummaryDTO>> getAllUsersSummary () {
        return ResponseEntity.ok(this.userService.getAllUsersSummary());
    }

}
