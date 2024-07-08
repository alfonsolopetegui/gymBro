package com.myCompany.gymBro.persistence.repository;


import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface ScheduleRepository extends ListCrudRepository<ScheduleEntity, UUID> {

}
