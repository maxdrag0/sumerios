package com.mad.sumerios.login.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Método para hashear/cifrar la contraseña
    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    // Método para comparar la contraseña sin cifrar con la cifrada en la base de datos
    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}

