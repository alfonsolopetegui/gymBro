package com.myCompany.gymBro.persistence.repository;


import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends ListCrudRepository<ScheduleEntity, UUID> {

    public List<ScheduleEntity> findAllByClassType_ClassId(UUID classId);

}
