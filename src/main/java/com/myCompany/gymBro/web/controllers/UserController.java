package com.myCompany.gymBro.web.controllers;

import com.myCompany.gymBro.service.UserService;
import com.myCompany.gymBro.service.dto.UserCreationDTO;
import com.myCompany.gymBro.service.dto.UserDetailsDTO;
import com.myCompany.gymBro.service.dto.UserSummaryDTO;
import com.myCompany.gymBro.service.dto.UserUpdateDTO;
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

    //Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDetailsDTO>>> getAllUsers() {
        ApiResponse<List<UserDetailsDTO>> response = this.userService.getAllUsers();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    //Obtener todos los usuarios en formato Summary
    @GetMapping("/summary")
    public ResponseEntity<List<UserSummaryDTO>> getAllUsersSummary () {
        return ResponseEntity.ok(this.userService.getAllUsersSummary());
    }

    //Obtener un usuario por su ID
    @GetMapping("/summary/{userId}")
    public ResponseEntity<ApiResponse<UserSummaryDTO>> getUser(@PathVariable String userId) {
        ApiResponse<UserSummaryDTO> response = this.userService.getUser(userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    //Crear un usuario
    @PostMapping
    public ResponseEntity<ApiResponse<UserSummaryDTO>> saveUser(@RequestBody UserCreationDTO user) {
        ApiResponse<UserSummaryDTO> response = this.userService.saveUser(user);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    //Editar un usuario
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserDetailsDTO>> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        ApiResponse<UserDetailsDTO> response = this.userService.updateUser(userUpdateDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    //Eliminar un usuario
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        ApiResponse<Void> response = this.userService.deleteUser(userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

}
