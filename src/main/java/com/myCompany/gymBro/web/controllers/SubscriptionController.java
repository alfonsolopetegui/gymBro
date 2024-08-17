package com.myCompany.gymBro.web.controllers;


import com.myCompany.gymBro.service.SubscriptionService;
import com.myCompany.gymBro.service.dto.SubscriptionCreationDTO;
import com.myCompany.gymBro.service.dto.SubscriptionDTO;
import com.myCompany.gymBro.web.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Subscription Controller", description = "Operaciones relacionadas con las subscripciones posibles al gimnasio")
@Validated
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las subscripciones", description = "Retorna una lista de las subscripciones disponibles en el gimnasio en formato SubscriptionDTO")
    public ResponseEntity<ApiResponse<List<SubscriptionDTO>>> getAllSubscriptions() {
        ApiResponse<List<SubscriptionDTO>> response = this.subscriptionService.getAllSubscriptions();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/save")
    @Operation(summary = "Guardar una nueva subscripción", description = "Guarda una nueva subscripción en la base datos")
    public ResponseEntity<ApiResponse<SubscriptionDTO>> saveSubscription(
            @Parameter(
                    description = "Datos de la subscripción a guradar, en formato SubscriptionCreationDTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SubscriptionCreationDTO.class))
            )
            @RequestBody @Valid SubscriptionCreationDTO subscriptionCreationDTO) {

        System.out.println("Entro al controlador");

        ApiResponse<SubscriptionDTO> response = this.subscriptionService.saveSubscription(subscriptionCreationDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/delete/{subscriptionId}")
    @Operation(summary = "Eliminar una subscripción existente", description = "Elimina una subscripción existente de la base de datos")
    public ResponseEntity<ApiResponse<Void>> deleteSubscription(
            @Parameter(description = "Id de la subscripción a eliminar", example = "be32593a-e6f9-4255-95ec-8353399deb05", required = true)
            @PathVariable String subscriptionId) {
        ApiResponse<Void> response = this.subscriptionService.deleteSubscription(subscriptionId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/update")
    @Operation(summary = "Editar Subscripción", description = "Editar datos de una subscripción existente")
    public ResponseEntity<ApiResponse<SubscriptionDTO>> updateSubscription(
            @Parameter(
                    description = "Datos de la subscripción a editar, en formato SubscriptionDTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SubscriptionDTO.class))
            )
            @RequestBody @Valid SubscriptionDTO subscriptionDTO) {
        ApiResponse<SubscriptionDTO> response = this.subscriptionService.updateSubscription(subscriptionDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

}
