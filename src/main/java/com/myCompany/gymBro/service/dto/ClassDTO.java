package com.myCompany.gymBro.service.dto;

import com.myCompany.gymBro.persistence.entity.ClassEntity;
import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ClassDTO {

    private UUID classId;

    @Schema(example = "Pilates")
    private String className;

    @Schema(example = "Trabajo de fuerza y elasticidad")
    private String classDescription;

    private List<ScheduleSummaryDTO> schedules;

    public ClassDTO(ClassEntity classEntity) {
        this.classDescription = classEntity.getClassDescription();
        this.classId = classEntity.getClassId();
        this.className = classEntity.getClassName();
        if (classEntity.getSchedules() != null && !classEntity.getSchedules().isEmpty()) {
            List<ScheduleSummaryDTO> scheduleSummaryDTOList = new ArrayList<>();
            for (ScheduleEntity sch : classEntity.getSchedules()) {
                ScheduleSummaryDTO scheduleSummaryDTO = new ScheduleSummaryDTO(sch);
                scheduleSummaryDTOList.add(scheduleSummaryDTO);
            }
            this.schedules = scheduleSummaryDTOList;
        } else {
            this.schedules = new ArrayList<>();
        }
    }
}
