package com.myCompany.gymBro.persistence;

import com.myCompany.gymBro.persistence.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "schedule_id", nullable = false)
    private UUID scheduleId;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classType;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<UserRegistrationEntity> userRegistrations;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

}
