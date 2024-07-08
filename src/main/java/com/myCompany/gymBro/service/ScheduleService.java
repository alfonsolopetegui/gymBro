package com.myCompany.gymBro.service;

import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import com.myCompany.gymBro.persistence.repository.ScheduleRepository;
import com.myCompany.gymBro.service.dto.ScheduleDTO;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ApiResponse<List<ScheduleDTO>> getAllSchedules() {
        List<ScheduleEntity> schedules = scheduleRepository.findAll();

        List<ScheduleDTO> scheduleDTOList = schedules.stream()
                .map(ScheduleDTO::new)
                .collect(Collectors.toList());

        return new ApiResponse<>("Se muestra la lista de horarios", 200, scheduleDTOList);
    }

}
