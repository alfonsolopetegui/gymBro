package com.myCompany.gymBro.persistence.repository;


import com.myCompany.gymBro.persistence.entity.UserEntity;
import com.myCompany.gymBro.persistence.enums.UserRole;
import org.apache.catalina.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends ListCrudRepository<UserEntity, UUID> {

    public List<UserEntity> getAllByUserRole(UserRole userRole);

    public Optional<UserEntity> findFirstByEmail(String email);

}
