package com.mad.sumerios.utils;

public class Validation {


    // Método para validar si un string es nulo o vacío
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Método para validar un correo electrónico
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }


}
