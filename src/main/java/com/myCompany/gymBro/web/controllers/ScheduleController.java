package com.myCompany.gymBro.web.controllers;

import com.myCompany.gymBro.service.ScheduleService;
import com.myCompany.gymBro.service.dto.*;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
@Tag(name = "Schedule Controller", description = "Operaciones relacionadas con los horarios disponibles a cada clase")

@Validated
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los horarios", description = "Retorna una lista de los horarios a clases disponibles en el gimnasio en formato ScheduleDTO")
    public ResponseEntity<ApiResponse<List<ScheduleDTO>>> getAllSchedules() {
        ApiResponse<List<ScheduleDTO>> response = this.scheduleService.getAllSchedules();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("byClassId/{classId}")
    @Operation(summary = "Obtener todos los horarios de una clase", description = "Retorna una lista de los horarios a una clase específica, por su Id, en formato ScheduleSummaryDTO")
    public ResponseEntity<ApiResponse<List<ScheduleSummaryDTO>>> getAllByClassId(
            @Parameter(description = "Id de la clase", example = "be32593a-e6f9-4255-95ec-8353399deb05", required = true)
            @PathVariable UUID classId) {
        ApiResponse<List<ScheduleSummaryDTO>> response = this.scheduleService.getAllByClassId(classId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/save")
    @Operation(summary = "Guardar un nuevo horario", description = "Guarda un nuevo horario a una clase en la base de datos, ingresando los datos utilizando el ScheduleCreationDTO")
    public ResponseEntity<ApiResponse<ScheduleCreationDTO>> saveSchedule(
            @Parameter(
                    description = "Datos del horario a guardar, en formato ScheduleCreationDTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ScheduleCreationDTO.class))
            )
            @RequestBody @Valid ScheduleCreationDTO scheduleCreationDTO) {
        ApiResponse<ScheduleCreationDTO> response = this.scheduleService.saveSchedule(scheduleCreationDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/delete/{scheduleId}")
    @Operation(summary = "Eliminar un horario existente", description = "Elimina un horario a una clase existente de la base de datos")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(
            @Parameter(description = "Id del horario a eliminar", example = "be32593a-e6f9-4255-95ec-8353399deb05", required = true)
            @PathVariable String scheduleId) {
        ApiResponse<Void> response = this.scheduleService.deleteSchedule(scheduleId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/update")
    @Operation(summary = "Editar un horario existente", description = "Editar datos de un horario existente, ingresando datos con el formato ScheduleUpdateDTO")
    public ResponseEntity<ApiResponse<ScheduleUpdateDTO>> updateSchedule(
            @Parameter(
                    description = "Datos del horario a editar, en formato ScheduleUpdateDTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ScheduleUpdateDTO.class))
            )
            @RequestBody @Valid ScheduleUpdateDTO scheduleUpdateDTO) {
        ApiResponse<ScheduleUpdateDTO> response = this.scheduleService.updateSchedule(scheduleUpdateDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

}
