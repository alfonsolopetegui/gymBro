package com.myCompany.gymBro.service;

import com.myCompany.gymBro.exception.CustomClassNotFoundException;
import com.myCompany.gymBro.persistence.entity.ClassEntity;
import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import com.myCompany.gymBro.persistence.repository.ClassRepository;
import com.myCompany.gymBro.service.dto.ClassCreationDTO;
import com.myCompany.gymBro.service.dto.ClassDTO;
import com.myCompany.gymBro.service.dto.ClassUpdateDTO;
import com.myCompany.gymBro.service.dto.ScheduleSummaryDTO;
import com.myCompany.gymBro.utils.ValidationUtils;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ClassService {
    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public ApiResponse<List<ClassDTO>> getAllClasses() {
        List<ClassEntity> classEntities = this.classRepository.findAll();
        if (classEntities.isEmpty()) {
            return new ApiResponse<>("No hay clases para mostrar", 404, null);
        }

        List<ClassDTO> classDTOList = new ArrayList<>();
        // por cada classEntity creo un classDTO
        for (ClassEntity c : classEntities) {
            ClassDTO classDTO = new ClassDTO();
            classDTO.setClassId(c.getClassId());
            classDTO.setClassName(c.getClassName());
            classDTO.setClassDescription(c.getClassDescription());

            // Transformo los ScheduleEntity en ScheduleSummaryDTO
            if (c.getSchedules() != null && !c.getSchedules().isEmpty()) {
                List<ScheduleSummaryDTO> scheduleSummaryDTOList = new ArrayList<>();
                for (ScheduleEntity sch : c.getSchedules()) {
                    ScheduleSummaryDTO scheduleSummaryDTO = new ScheduleSummaryDTO(sch);
                    scheduleSummaryDTOList.add(scheduleSummaryDTO);
                }
                classDTO.setSchedules(scheduleSummaryDTOList);
            } else {
                classDTO.setSchedules(new ArrayList<>());
            }

            classDTOList.add(classDTO);
        }

        return new ApiResponse<>("Mostrando lista de clases", 200, classDTOList);
    }

    public ApiResponse<ClassDTO> getByName(String name) {
        Optional<ClassEntity> classEntity = this.classRepository.findFirstByClassNameIgnoreCase(name);
        if (classEntity.isPresent()) {
            ClassDTO classDTO = new ClassDTO(classEntity.get());
            return new ApiResponse<>("Clase encontrada", 200, classDTO);
        } else {
            return new ApiResponse<>("La clase que buscas no existe", 404, null);
        }
    }

    public ApiResponse<ClassDTO> saveClass(ClassCreationDTO classCreationDTO) {
        // Verificar que classCreationDTO no sea nulo
        if (classCreationDTO == null) {
            return new ApiResponse<>("Los datos de la clase son obligatorios", 400, null);
        }

        // Validación de entrada
        if (classCreationDTO.getClassName() == null || classCreationDTO.getClassName().isEmpty()) {
            return new ApiResponse<>("El nombre de la clase es obligatorio", 400, null);
        }

        // Verificar si el nombre de la clase ya existe (ignorando mayúsculas y minúsculas)
        if (this.classRepository.existsByClassNameIgnoreCase(classCreationDTO.getClassName())) {
            return new ApiResponse<>("El nombre que ingresaste ya está en uso", 400, null);
        }

        // Crear la entidad de la clase
        ClassEntity classEntity = new ClassEntity();
        classEntity.setClassName(classCreationDTO.getClassName());
        classEntity.setClassDescription(classCreationDTO.getClassDescription());
        classEntity.setIsActive(true);

        try {
            // Guardar la clase en el repositorio
            ClassEntity savedClass = this.classRepository.save(classEntity);
            // Crear el DTO de la clase
            ClassDTO classDTO = new ClassDTO(savedClass);
            // Devolver la respuesta de éxito
            return new ApiResponse<>("Clase creada exitosamente", 200, classDTO);
        } catch (RuntimeException e) {
            e.printStackTrace();
            // Devolver la respuesta de error
            return new ApiResponse<>("Error al crear la clase", 500, null);
        }
    }

    public ApiResponse<Void> deleteClass(String classId) {

        if (!ValidationUtils.isValidUUID(classId)) {
            return new ApiResponse<>("El ID no es un UUID válido", 404, null);
        }

        UUID id = UUID.fromString(classId);

        if (!this.classRepository.existsById(id)) {
            throw new CustomClassNotFoundException("La clase que buscas no existe");
        }

        try {
            this.classRepository.deleteById(id);
            return new ApiResponse<>("Clase borrada exitosamente", 200, null);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ApiResponse<>("Error al borrar la clase", 500, null);
        }

    }

    public ApiResponse<ClassUpdateDTO> updateClass(ClassUpdateDTO classUpdateDTO) {

        // Validación del ID
        if (!ValidationUtils.isValidUUID(String.valueOf(classUpdateDTO.getClassId()))) {
            return new ApiResponse<>("El ID no es un UUID válido", 404, null);
        }
        UUID id = UUID.fromString(String.valueOf(classUpdateDTO.getClassId()));

        // Búsqueda de la clase
        ClassEntity existingClass = this.classRepository.findById(id)
                .orElseThrow(() -> new CustomClassNotFoundException("La clase que buscas no existe"));

        // Validación del nombre de la clase
        if (classUpdateDTO.getClassName().isEmpty()) {
            return new ApiResponse<>("El nombre de la clase no puede estar vacío", 400, null);
        }

        // Actualización de los campos
        if (classUpdateDTO.getIsActive() != existingClass.getIsActive()) {
            existingClass.setIsActive(classUpdateDTO.getIsActive());
        }

        existingClass.setClassName(classUpdateDTO.getClassName());
        existingClass.setClassDescription(classUpdateDTO.getClassDescription());

        try {
            // Guardado de la clase actualizada
            ClassEntity updatedClass = this.classRepository.save(existingClass);
            ClassUpdateDTO classUpdateResponse = new ClassUpdateDTO(updatedClass);
            return new ApiResponse<>("Clase modificada con éxito", 200, classUpdateResponse);
        } catch (Exception e) {
            // Manejo de cualquier excepción durante el guardado
            return new ApiResponse<>("Ocurrió un error al guardar la clase actualizada", 500, null);
        }
    }



}
