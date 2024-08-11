package com.myCompany.gymBro.persistence.entity;

import com.myCompany.gymBro.persistence.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "schedule_day")
@Getter
@Setter
@NoArgsConstructor
public class ScheduleDayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "schedule_day_id", nullable = false, updatable = false)
    private UUID scheduleDayId;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "day_name")
    private DayOfWeek day;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    @Override
    public String toString() {
        return "ScheduleDayEntity{" +
                "day=" + day +
                ", scheduleDayId=" + scheduleDayId +
                ", schedule=" + schedule +
                '}';
    }
}
