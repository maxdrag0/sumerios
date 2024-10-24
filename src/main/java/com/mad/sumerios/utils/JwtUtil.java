package com.mad.sumerios.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    // Método para generar el JWT
    public String generateJwtToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Usar la clave en formato seguro
                .compact();
    }

    // Método para extraer el nombre de usuario del token JWT
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())  // Usar la clave en formato seguro
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Método para validar el token JWT
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Token JWT no válido: " + e.getMessage());
        }
        return false;
    }

    // Método auxiliar para convertir la clave en un formato seguro
    private Key getSigningKey() {
        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecret);  // Decodificar la clave secreta en Base64
        return new SecretKeySpec(secretKeyBytes, 0, secretKeyBytes.length, "HmacSHA512");  // Crear una clave segura
    }
}


