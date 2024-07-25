package com.myCompany.gymBro.web.controllers;


import com.myCompany.gymBro.service.SubscriptionService;
import com.myCompany.gymBro.service.dto.SubscriptionCreationDTO;
import com.myCompany.gymBro.service.dto.SubscriptionDTO;
import com.myCompany.gymBro.web.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubscriptionDTO>>> getAllSubscriptions() {
        ApiResponse<List<SubscriptionDTO>> response = this.subscriptionService.getAllSubscriptions();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<SubscriptionDTO>> saveSubscription(@RequestBody SubscriptionCreationDTO subscriptionCreationDTO) {
        ApiResponse<SubscriptionDTO> response = this.subscriptionService.saveSubscription(subscriptionCreationDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/delete/{subscriptionId}")
    public ResponseEntity<ApiResponse<Void>> deleteSubscription(@PathVariable String subscriptionId) {
        ApiResponse<Void> response = this.subscriptionService.deleteSubscription(subscriptionId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<SubscriptionDTO>> updateSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        ApiResponse<SubscriptionDTO> response = this.subscriptionService.updateSubscription(subscriptionDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

}
