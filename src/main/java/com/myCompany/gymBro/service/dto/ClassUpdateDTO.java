package com.myCompany.gymBro.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassUpdateDTO {

    private String classId;
    private String className;
    private String classDescription;
    private Boolean isActive;
}
