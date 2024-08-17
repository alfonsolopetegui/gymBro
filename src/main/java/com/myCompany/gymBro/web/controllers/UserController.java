package com.myCompany.gymBro.web.controllers;

import com.myCompany.gymBro.exception.AccessDeniedException;
import com.myCompany.gymBro.service.UserService;
import com.myCompany.gymBro.service.dto.*;
import com.myCompany.gymBro.web.config.JwtUtils;
import com.myCompany.gymBro.web.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Operaciones relacionadas con los usuarios del gimnasio")
@Validated
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    //Obtener todos los usuarios
    @GetMapping
    @Operation(summary = "Obtener todas los usuarios", description = "Retorna una lista de los usuarios disponibles en el gimnasio en formato UserDetailsDTO")
    public ResponseEntity<ApiResponse<List<UserDetailsDTO>>> getAllUsers() {
        ApiResponse<List<UserDetailsDTO>> response = this.userService.getAllUsers();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    //Obtener todos los usuarios en formato Summary
    @GetMapping("/summary")
    @Operation(summary = "Obtener todas los usuarios", description = "Retorna una lista de los usuarios disponibles en el gimnasio en formato UserSummaryDTO")
    public ResponseEntity<List<UserSummaryDTO>> getAllUsersSummary () {
        return ResponseEntity.ok(this.userService.getAllUsersSummary());
    }


    //Obtener un usuario por su ID
    @GetMapping("/summary/{userId}")
    @Operation(summary = "Obtener un usuario por su Id", description = "Retorna un usuario por su Id, en formato userSummaryDTO")
    public ResponseEntity<ApiResponse<UserSummaryDTO>> getUser(
            @Parameter(description = "Id del usuario", example = "4e91531c-e4fb-40b1-9272-a8c911f7cbff", required = true)
            @PathVariable String userId,
            @RequestHeader("Authorization") String authHeader)  {

        String token = authHeader.substring(7);  // Quitar "Bearer " del encabezado
        String authenticatedUserId = jwtUtils.extractUserId(token);

        if (!authenticatedUserId.equals(userId)) {
           throw new AccessDeniedException("No tienes permisos para ver estos datos");
        }

        ApiResponse<UserSummaryDTO> response = this.userService.getUser(userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    //Crear un usuario
    @PostMapping("/save")
    @Operation(summary = "Guardar un usuario nuevo", description = "Guarda un nuevo usuario en la base de datos")
    public ResponseEntity<ApiResponse<UserSummaryDTO>> saveUser(
            @Parameter(
                    description = "Datos del usuario a guradar, en formato UserCreationDTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserCreationDTO.class))
            )
            @RequestBody @Valid UserCreationDTO user) {

        System.out.println("Entró al controlador");

        ApiResponse<UserSummaryDTO> response = this.userService.saveUser(user);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    //Editar un usuario
    @PutMapping("/update")
    @Operation(summary = "Editar Usuario", description = "Editar datos de un usuario existente")
    public ResponseEntity<ApiResponse<UserDetailsDTO>> updateUser(
            @Parameter(
                    description = "Datos del usuario a editar, en formato UserUpdateDTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserUpdateDTO.class))
            )
            @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        ApiResponse<UserDetailsDTO> response = this.userService.updateUser(userUpdateDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    //Eliminar un usuario
    @DeleteMapping("/delete/{userId}")
    @Operation(summary = "Eliminar un usuario existente", description = "Elimina un usuario existente de la base de datos")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "Id del usuario a eliminar", example = "be32593a-e6f9-4255-95ec-8353399deb05", required = true)
            @PathVariable String userId) {
        ApiResponse<Void> response = this.userService.deleteUser(userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

}
