package com.myCompany.gymBro.persistence.repository;


import com.myCompany.gymBro.persistence.entity.ClassEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClassRepository extends ListCrudRepository<ClassEntity, UUID> {

    public Optional<ClassEntity> findFirstByClassNameIgnoreCase(String name);

    boolean existsByClassNameIgnoreCase(String name);

}
