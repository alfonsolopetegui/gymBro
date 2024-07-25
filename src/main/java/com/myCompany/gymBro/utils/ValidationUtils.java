package com.myCompany.gymBro.utils;

import com.myCompany.gymBro.persistence.enums.NumberOfClasses;

import java.time.LocalTime;
import java.util.UUID;

public class ValidationUtils {

    // Método para validar si una cadena es un UUID válido
    public static boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isValidTime(String time) {
        String timePattern = "^(?:[01]\\d|2[0-3]):[0-5]\\d$";
        return time != null && time.matches(timePattern);
    }

    public static boolean isEndTimeAfterStartTime(LocalTime startTime, LocalTime endTime) {
        return endTime.isAfter(startTime);
    }

    public static boolean isValidNumberOfClasses(NumberOfClasses numberOfClasses) {
        return numberOfClasses != null; // O cualquier otra lógica que defina validez
    }

}
