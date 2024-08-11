package com.myCompany.gymBro.service;

import com.myCompany.gymBro.exception.CustomClassNotFoundException;
import com.myCompany.gymBro.exception.ScheduleNotFoundException;
import com.myCompany.gymBro.persistence.entity.ClassEntity;
import com.myCompany.gymBro.persistence.entity.ScheduleDayEntity;
import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import com.myCompany.gymBro.persistence.enums.DayOfWeek;
import com.myCompany.gymBro.persistence.repository.ClassRepository;
import com.myCompany.gymBro.persistence.repository.ScheduleRepository;
import com.myCompany.gymBro.service.dto.ScheduleCreationDTO;
import com.myCompany.gymBro.service.dto.ScheduleDTO;
import com.myCompany.gymBro.service.dto.ScheduleSummaryDTO;
import com.myCompany.gymBro.service.dto.ScheduleUpdateDTO;
import com.myCompany.gymBro.utils.ValidationUtils;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ClassRepository classRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, ClassRepository classRepository) {
        this.scheduleRepository = scheduleRepository;
        this.classRepository = classRepository;
    }

    public ApiResponse<List<ScheduleDTO>> getAllSchedules() {
        List<ScheduleEntity> schedules = scheduleRepository.findAll();

        List<ScheduleDTO> scheduleDTOList = schedules.stream()
                .map(ScheduleDTO::new)
                .collect(Collectors.toList());

        return new ApiResponse<>("Se muestra la lista de horarios", 200, scheduleDTOList);
    }

    public ApiResponse<ScheduleCreationDTO> saveSchedule(ScheduleCreationDTO scheduleCreationDTO) {

        //Valida que UUID sea correcto
        if (!ValidationUtils.isValidUUID(String.valueOf(scheduleCreationDTO.getClassId()))) {
            return new ApiResponse<>("El ID no es un UUID válido", 404, null);
        }

        UUID classId = UUID.fromString(String.valueOf(scheduleCreationDTO.getClassId()));

        //Busca que la clase exista
        ClassEntity classType = this.classRepository.findById(classId)
                .orElseThrow(() -> new CustomClassNotFoundException("No existe una clase con ese ID"));

        // Validación de tiempos
        if (scheduleCreationDTO.getStartTime().isAfter(scheduleCreationDTO.getEndTime())) {
            return new ApiResponse<>("La hora de inicio debe ser anterior a la hora de fin", 400, null);
        }

        //Creo la instancia del Schedule y la asigno los datos
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setStartTime(scheduleCreationDTO.getStartTime());
        scheduleEntity.setEndTime(scheduleCreationDTO.getEndTime());
        scheduleEntity.setClassType(classType);
        scheduleEntity.setMaxRegistrations(scheduleCreationDTO.getMaxRegistrations());
        scheduleEntity.setUserRegistrations(new ArrayList<>());

        List<ScheduleDayEntity> scheduleDayEntityList = new ArrayList<>();
        for (String day : scheduleCreationDTO.getDays()) {
            ScheduleDayEntity scheduleDayEntity = new ScheduleDayEntity();
            scheduleDayEntity.setDay(DayOfWeek.valueOf(day));
            scheduleDayEntity.setSchedule(scheduleEntity);
            scheduleDayEntityList.add(scheduleDayEntity);
        }

        scheduleEntity.setDays(scheduleDayEntityList);

        try {
            ScheduleEntity savedSchedule = this.scheduleRepository.save(scheduleEntity);
            ScheduleCreationDTO scheduleCreationResponse = new ScheduleCreationDTO(savedSchedule);
            return new ApiResponse<>("Schedule guardado correctamente", 200, scheduleCreationResponse);
        } catch (RuntimeException e) {
            e.printStackTrace(); // Podrías registrar esto en lugar de imprimir
            return new ApiResponse<>("Error al guardar el schedule", 500, null);
        }
    }

    public ApiResponse<Void> deleteSchedule (String scheduleId) {

        if(!ValidationUtils.isValidUUID(scheduleId)) {
            return new ApiResponse<>("El ID proporcionado no es un UUID válido", 404, null);
        }

        System.out.println("pasó el validador");

        UUID id = UUID.fromString(scheduleId);

        System.out.println("llegó aquí");

        if (!this.scheduleRepository.existsById(id)) {
            throw new ScheduleNotFoundException("El schedule que buscas no existe");
        }

        try {
            this.scheduleRepository.deleteById(id);
            return new ApiResponse<>("Schedule eliminado exitosamente", 200, null);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ApiResponse<>("Error al eliminar el schedule", 500, null);
        }
    }

    //Falta probar
    public ApiResponse<ScheduleUpdateDTO> updateSchedule(ScheduleUpdateDTO scheduleUpdateDTO) {


        // Validar UUIDs
        if (!ValidationUtils.isValidUUID(String.valueOf(scheduleUpdateDTO.getScheduleId()))) {
            return new ApiResponse<>("El Id proporcionado del schedule no es un UUID válido", 400, null);
        }
        if (!ValidationUtils.isValidUUID(String.valueOf(scheduleUpdateDTO.getClassId()))) {
            return new ApiResponse<>("El Id proporcionado de la clase no es un UUID válido", 400, null);
        }

        // Validar tiempos
        if (!ValidationUtils.isValidTime(String.valueOf(scheduleUpdateDTO.getStartTime()))) {
            return new ApiResponse<>("El tiempo de inicio no tiene un formato válido", 400, null);
        }
        if (!ValidationUtils.isValidTime(String.valueOf(scheduleUpdateDTO.getEndTime()))) {
            return new ApiResponse<>("El tiempo de fin no tiene un formato válido", 400, null);
        }
        if (!ValidationUtils.isEndTimeAfterStartTime(scheduleUpdateDTO.getStartTime(), scheduleUpdateDTO.getEndTime())) {
            return new ApiResponse<>("El tiempo de fin debe ser después del tiempo de inicio", 400, null);
        }

        // Validar maxRegistrations
        if (scheduleUpdateDTO.getMaxRegistrations() <= 0) {
            return new ApiResponse<>("El número máximo de registros debe ser positivo", 400, null);
        }

        // Validar días de la semana
        Set<String> validDays = Arrays.stream(DayOfWeek.values())
                .map(DayOfWeek::name)
                .collect(Collectors.toSet());
        for (String day : scheduleUpdateDTO.getDays()) {
            if (!validDays.contains(day)) {
                return new ApiResponse<>("Día inválido: " + day, 400, null);
            }
        }

        UUID scheduleId = UUID.fromString(String.valueOf(scheduleUpdateDTO.getScheduleId()));
        ScheduleEntity existingSchedule = this.scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("El schedule que buscas no existe"));

        UUID classId = UUID.fromString(String.valueOf(scheduleUpdateDTO.getClassId()));
        ClassEntity classEntity = this.classRepository.findById(classId)
                .orElseThrow(() -> new CustomClassNotFoundException("La clase que buscas no existe"));

        List<ScheduleDayEntity> scheduleDayEntityList = new ArrayList<>();
        for (String day : scheduleUpdateDTO.getDays()) {
            ScheduleDayEntity scheduleDayEntity = new ScheduleDayEntity();
            scheduleDayEntity.setDay(DayOfWeek.valueOf(day));
            scheduleDayEntityList.add(scheduleDayEntity);
        }

        existingSchedule.setDays(scheduleDayEntityList);
        existingSchedule.setClassType(classEntity);
        existingSchedule.setMaxRegistrations(scheduleUpdateDTO.getMaxRegistrations());
        existingSchedule.setStartTime(scheduleUpdateDTO.getStartTime());
        existingSchedule.setEndTime(scheduleUpdateDTO.getEndTime());

        try {
            ScheduleEntity updatedSchedule = this.scheduleRepository.save(existingSchedule);
            ScheduleUpdateDTO scheduleResponse = new ScheduleUpdateDTO(updatedSchedule);
            return new ApiResponse<>("Schedule modificado con exito", 200, scheduleResponse);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ApiResponse<>("Error al modificar el schedule", 500, null);
        }
    }

    public ApiResponse<List<ScheduleSummaryDTO>> getAllByClassId(UUID classId) {

        List<ScheduleEntity> scheduleEntities = this.scheduleRepository.findAllByClassType_ClassId(classId);

        List<ScheduleSummaryDTO> scheduleSummaryDTOList = new ArrayList<>();
        for (ScheduleEntity schedule : scheduleEntities) {
            ScheduleSummaryDTO scheduleSummaryDTO = new ScheduleSummaryDTO(schedule);
            scheduleSummaryDTOList.add(scheduleSummaryDTO);
        }

        return new ApiResponse<>("Lista de Schedules por clase", 200, scheduleSummaryDTOList);
    }


}
