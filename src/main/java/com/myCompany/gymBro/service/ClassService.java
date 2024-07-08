package com.myCompany.gymBro.service;

import com.myCompany.gymBro.persistence.entity.ClassEntity;
import com.myCompany.gymBro.persistence.repository.ClassRepository;
import com.myCompany.gymBro.web.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassService {
    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public ApiResponse<List<ClassEntity>> getAllClasses() {
        List<ClassEntity> classEntities = this.classRepository.findAll();
        if(!classEntities.isEmpty()) {
            return new ApiResponse<>("Lista de clases encontrada", 200, classEntities);
        } else {
            return new ApiResponse<>("No hay clases para mostrar", 404, null);
        }
    }

    public ApiResponse<ClassEntity> getByName(String name) {
        Optional<ClassEntity> classEntity = this.classRepository.findFirstByClassNameIgnoreCase(name);
        if (classEntity.isPresent()) {
            return new ApiResponse<>("Clase encontrada", 200, classEntity.get());
        } else {
            return new ApiResponse<>("La clase que buscas no existe", 404, null);
        }
    }
}
