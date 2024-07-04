package com.myCompany.gymBro.web.controllers;

import com.myCompany.gymBro.persistence.entity.UserEntity;
import com.myCompany.gymBro.service.UserService;
import com.myCompany.gymBro.service.dto.UserSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/summary")
    public ResponseEntity<List<UserSummaryDTO>> getAllUsersSummary () {
        return ResponseEntity.ok(this.userService.getAllUsersSummary());
    }

}
