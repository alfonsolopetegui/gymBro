package com.myCompany.gymBro.web.controllers;


import com.myCompany.gymBro.persistence.entity.ClassEntity;
import com.myCompany.gymBro.service.ClassService;
import com.myCompany.gymBro.service.dto.ClassCreationDTO;
import com.myCompany.gymBro.service.dto.ClassDTO;
import com.myCompany.gymBro.service.dto.ClassUpdateDTO;
import com.myCompany.gymBro.web.response.ApiResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClassDTO>>> getAllClasses() {
        ApiResponse<List<ClassDTO>> response = classService.getAllClasses();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<ClassDTO>> getByName(@PathVariable String name) {
        ApiResponse<ClassDTO> response = classService.getByName(name);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<ClassDTO>> saveClass(@RequestBody ClassCreationDTO classCreationDTO) {
        ApiResponse<ClassDTO> response = this.classService.saveClass(classCreationDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ClassEntity>> updateClass(@RequestBody ClassUpdateDTO classUpdateDTO) throws ClassNotFoundException {
        ApiResponse<ClassEntity> response = this.classService.updateClass(classUpdateDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/delete/{classId}")
    public ResponseEntity<ApiResponse<Void>> deleteCLass(@PathVariable String classId) throws ClassNotFoundException {
        ApiResponse<Void> response = this.classService.deleteClass(classId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
