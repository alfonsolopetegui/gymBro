package com.myCompany.gymBro.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserSummaryDTO {
    private UUID userId;
    private String username;
    private String email;
    private String userRole;
}
