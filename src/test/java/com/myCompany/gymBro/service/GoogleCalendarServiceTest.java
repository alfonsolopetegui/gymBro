package com.myCompany.gymBro.service;

import com.myCompany.gymBro.persistence.entity.ClassEntity;
import com.myCompany.gymBro.persistence.entity.ScheduleEntity;
import com.myCompany.gymBro.persistence.repository.GoogleTokenRepository;
import com.myCompany.gymBro.service.dto.EventDTO;
import com.myCompany.gymBro.service.dto.RegistrationCreationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class GoogleCalendarServiceTest {

    @Mock
    private GoogleTokenRepository googleTokenRepository;

    @InjectMocks
    private GoogleCalendarService googleCalendarService;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        googleCalendarService = new GoogleCalendarService(googleTokenRepository); // Crea una instancia real
    }


    @Test
    void createEventDTO() {
        // Crear ScheduleEntity y dependencias
        ScheduleEntity scheduleEntity = new ScheduleEntity();


        List<ScheduleEntity> schedulesList = new ArrayList<>();
        schedulesList.add(scheduleEntity);

        List<DayOfWeek> daysList = new ArrayList<>();
        daysList.add(DayOfWeek.MONDAY);
        daysList.add(DayOfWeek.WEDNESDAY);

        ClassEntity classEntity = new ClassEntity();
        classEntity.setClassDescription("Description");
        classEntity.setClassName("Boxing");
        classEntity.setClassId(UUID.randomUUID());
        classEntity.setSchedules(schedulesList);

        scheduleEntity.setDays(daysList);
        scheduleEntity.setMaxRegistrations(10);
        scheduleEntity.setStartTime(LocalTime.of(11, 0));
        scheduleEntity.setEndTime(LocalTime.of(12, 0));
        scheduleEntity.setClassType(classEntity);
        scheduleEntity.setScheduleId(UUID.randomUUID());


        RegistrationCreationDTO registrationCreationDTO = new RegistrationCreationDTO();
        registrationCreationDTO.setUserId(UUID.randomUUID());
        registrationCreationDTO.setScheduleId(scheduleEntity.getScheduleId());


        // Depuración: Imprimir valores de los objetos antes de llamar al método
        System.out.println("ScheduleEntity: " + scheduleEntity);
        System.out.println("ClassEntity: " + classEntity);
        System.out.println("Days: " + daysList);
        System.out.println("RegistrationCreationDTO: " + registrationCreationDTO);


        // Llamar al método createEventDTO
        EventDTO eventDTO = googleCalendarService.createEventDTO(scheduleEntity, registrationCreationDTO);

        System.out.println("EventDTO: " + eventDTO);

        // Verificar resultados
        assertNotNull(eventDTO);
        assertEquals("Clase de Boxing", eventDTO.getSummary());
        assertEquals("GymBro", eventDTO.getLocation());
        assertEquals("Description", eventDTO.getDescription());
        assertEquals(2, eventDTO.getDays().size());
        assertEquals("MO", eventDTO.getDays().get(0));
        assertEquals("WE", eventDTO.getDays().get(1));

        LocalDateTime expectedStartDate = LocalDateTime.now().withHour(11).withMinute(0).withSecond(0);
        LocalDateTime actualStartDate = LocalDateTime.parse(eventDTO.getStart().getDateTime());
        assertEquals(expectedStartDate.getHour(), actualStartDate.getHour());
        assertEquals(expectedStartDate.getMinute(), actualStartDate.getMinute());

        LocalDateTime expectedEndDate = expectedStartDate.plusHours(1);
        LocalDateTime actualEndDate = LocalDateTime.parse(eventDTO.getEnd().getDateTime());
        assertEquals(expectedEndDate.getHour(), actualEndDate.getHour());
        assertEquals(expectedEndDate.getMinute(), actualEndDate.getMinute());
    }


    @Test
    void calculateOriginalStartDate() {
        // Configurar los días de la semana en que se da la clase
        List<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.WEDNESDAY);

        // Configurar la hora de inicio de la clase
        LocalTime time = LocalTime.of(11, 0);

        // Llamar al método que se está probando
        EventDTO.EventDateTime startDate = googleCalendarService.calculateOriginalStartDate(days, time);

        // Obtener la fecha y hora actuales
        LocalDateTime now = LocalDateTime.now();

        // Inicializar la fecha esperada con la fecha y hora actuales
        LocalDateTime expectedDate = now;

        // Buscar el próximo día de clase y ajustar la hora
        while (!days.contains(expectedDate.getDayOfWeek()) || expectedDate.toLocalTime().isBefore(time)) {
            // Avanzar al siguiente día
            expectedDate = expectedDate.plusDays(1);
        }

        // Ajustar la hora de expectedDate a la hora de la clase
        expectedDate = expectedDate.withHour(time.getHour())
                .withMinute(time.getMinute())
                .withSecond(0)
                .withNano(0);

        // Truncar la parte de nanosegundos de startDate para la comparación
        LocalDateTime actualDate = LocalDateTime.parse(startDate.getDateTime()).truncatedTo(ChronoUnit.SECONDS);

        // Asegurarse de que la fecha calculada es la esperada
        assertEquals(expectedDate, actualDate, "La fecha de inicio calculada no es correcta");

        // Puedes imprimir las fechas para verificar visualmente si es necesario
        System.out.println("Expected: " + expectedDate);
        System.out.println("Actual: " + actualDate);
    }

}