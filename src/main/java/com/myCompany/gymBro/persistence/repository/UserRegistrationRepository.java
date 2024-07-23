package com.myCompany.gymBro.persistence.repository;

import com.myCompany.gymBro.persistence.entity.UserRegistrationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRegistrationRepository extends ListCrudRepository<UserRegistrationEntity, UUID> {

    @Query("SELECT CASE WHEN COUNT(ur) > 0 THEN TRUE ELSE FALSE END FROM UserRegistrationEntity ur WHERE ur.user.userId = :userId AND ur.schedule.scheduleId = :scheduleId")
    boolean existsByUserIdAndScheduleId(@Param("userId") UUID userId, @Param("scheduleId") UUID scheduleId);
}

