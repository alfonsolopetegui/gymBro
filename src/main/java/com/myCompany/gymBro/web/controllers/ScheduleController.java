package com.myCompany.gymBro.web.controllers;

import com.myCompany.gymBro.persistence.repository.ScheduleRepository;
import com.myCompany.gymBro.service.ScheduleService;
import com.myCompany.gymBro.service.dto.ScheduleDTO;
import com.myCompany.gymBro.service.dto.UserDetailsDTO;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ScheduleDTO>>> getAllSchedules() {
        ApiResponse<List<ScheduleDTO>> response = this.scheduleService.getAllSchedules();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
