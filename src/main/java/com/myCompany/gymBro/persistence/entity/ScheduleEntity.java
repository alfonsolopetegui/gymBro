package com.myCompany.gymBro.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
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

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserRegistrationEntity> userRegistrations;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "max_registrations", nullable = false)
    private int maxRegistrations;

    private List<DayOfWeek> days;

    public boolean isRegistrationFull() {
        return userRegistrations != null && userRegistrations.size() >= maxRegistrations;
    }

    @Override
    public String toString() {
        return "ScheduleEntity{" +
                "classType=" + (classType != null ? classType.getClassId() : null) +
                ", scheduleId=" + scheduleId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", maxRegistrations=" + maxRegistrations +
                ", days=" + (days != null ? days.stream().map(Enum::toString).toList() : "[]") +
                '}';
    }
}
