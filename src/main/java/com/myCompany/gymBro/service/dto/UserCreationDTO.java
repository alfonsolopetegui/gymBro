package com.myCompany.gymBro.service.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserCreationDTO {

    //DTO usado para la creación de usuarios. Con los datos requeridos

    private String username;
    private String email;
    private String password;
    private String subscriptionId;

}
