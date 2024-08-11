package com.myCompany.gymBro.web.controllers;

import com.myCompany.gymBro.persistence.entity.ClassEntity;
import com.myCompany.gymBro.service.ClassService;
import com.myCompany.gymBro.service.dto.ClassCreationDTO;
import com.myCompany.gymBro.service.dto.ClassDTO;
import com.myCompany.gymBro.service.dto.ClassUpdateDTO;
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

@RestController
@RequestMapping("/api/classes")
@Tag(name = "Class Controller", description = "Operaciones relacionadas con las clases disponibles en el gimnasio")
@Validated
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las clases", description = "Retorna una lista de las clases disponibles en el gimnasio")
    public ResponseEntity<ApiResponse<List<ClassDTO>>> getAllClasses() {
        ApiResponse<List<ClassDTO>> response = classService.getAllClasses();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Obtener una unica clase, por su nombre", description = "Retorna una clase, buscandola por su nombre en el path")
    public ResponseEntity<ApiResponse<ClassDTO>> getByName(
            @Parameter(description = "Nombre de la clase", example = "Zumba", required = true)  //Swagger
            @PathVariable String name) {
        ApiResponse<ClassDTO> response = classService.getByName(name);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/save")
    @Operation(summary = "Guardar una nueva clase", description = "Guarda una nueva clase en la base de datos, ingresando los datos utilizando el ClassCreationDTO")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Clase creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClassDTO.class))
    )
    public ResponseEntity<ApiResponse<ClassDTO>> saveClass(
            @Parameter(
                    description = "Datos de la clase a guardar, en formato ClassCreationDTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClassCreationDTO.class))
            )
            @RequestBody @Valid ClassCreationDTO classCreationDTO) {
        ApiResponse<ClassDTO> response = this.classService.saveClass(classCreationDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PutMapping("/update")
    @Operation(summary = "Editar una clase existente", description = "Editar datos de una clase existente, ingresando datos con el formato ClassUpdateDTO")
    public ResponseEntity<ApiResponse<ClassUpdateDTO>> updateClass(
            @Parameter(
                    description = "Datos de la clase a editar, en formato ClassUpdateDTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClassUpdateDTO.class))
            )
            @RequestBody @Valid ClassUpdateDTO classUpdateDTO) throws ClassNotFoundException {
        ApiResponse<ClassUpdateDTO> response = this.classService.updateClass(classUpdateDTO);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @DeleteMapping("/delete/{classId}")
    @Operation(summary = "Eliminar una clase existente", description = "Elimina una clase existente de la base de datos")
    public ResponseEntity<ApiResponse<Void>> deleteCLass(
            @Parameter(description = "Id de la clase a eliminar", example = "be32593a-e6f9-4255-95ec-8353399deb05", required = true)
            @PathVariable String classId) throws ClassNotFoundException {
        ApiResponse<Void> response = this.classService.deleteClass(classId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
