package com.myCompany.gymBro.persistence.entity;

import com.myCompany.gymBro.persistence.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
public class ScheduleEntity {

    //Esta entidad guarda fechas y horarios para clases específicas

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "schedule_id", nullable = false)
    private UUID scheduleId;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classType;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<UserRegistrationEntity> userRegistrations;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "max_registrations", nullable = false)
    private int maxRegistrations;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<ScheduleDayEntity> days;

    public boolean isRegistrationFull() {
        return userRegistrations != null && userRegistrations.size() >= maxRegistrations;
    }

}
