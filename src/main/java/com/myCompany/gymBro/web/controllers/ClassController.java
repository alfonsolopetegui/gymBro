package com.myCompany.gymBro.web.controllers;


import com.myCompany.gymBro.persistence.entity.ClassEntity;
import com.myCompany.gymBro.service.ClassService;
import com.myCompany.gymBro.web.response.ApiResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClassEntity>>> getAllClasses() {
        ApiResponse<List<ClassEntity>> response = classService.getAllClasses();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<ClassEntity>> getByName(@PathVariable String name) {
        ApiResponse<ClassEntity> response = classService.getByName(name);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
