package com.myCompany.gymBro.persistence.repository;

import com.myCompany.gymBro.persistence.entity.GoogleTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoogleTokenRepository extends CrudRepository<GoogleTokenEntity, UUID> {
    Optional<GoogleTokenEntity> findByUser_UserId(UUID userId);
}
