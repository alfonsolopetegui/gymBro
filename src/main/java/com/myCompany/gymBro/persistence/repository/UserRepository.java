package com.myCompany.gymBro.persistence.repository;


import com.myCompany.gymBro.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;


public interface UserRepository extends CrudRepository<UserEntity, UUID> {


}
