package com.myCompany.gymBro.web.controllers;

import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import com.myCompany.gymBro.persistence.repository.ScheduleRepository;
import com.myCompany.gymBro.service.ScheduleService;
import com.myCompany.gymBro.service.dto.ScheduleCreationDTO;
import com.myCompany.gymBro.service.dto.ScheduleDTO;
import com.myCompany.gymBro.service.dto.ScheduleUpdateDTO;
import com.myCompany.gymBro.service.dto.UserDetailsDTO;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<ScheduleCreationDTO>> saveSchedule(@RequestBody ScheduleCreationDTO scheduleCreationDTO) {
        ApiResponse<ScheduleCreationDTO> response = this.scheduleService.saveSchedule(scheduleCreationDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/delete/{scheduleId}")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(@PathVariable String scheduleId) {
        ApiResponse<Void> response = this.scheduleService.deleteSchedule(scheduleId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ScheduleUpdateDTO>> updateSchedule(@RequestBody ScheduleUpdateDTO scheduleUpdateDTO) {
        ApiResponse<ScheduleUpdateDTO> response = this.scheduleService.updateSchedule(scheduleUpdateDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

}
