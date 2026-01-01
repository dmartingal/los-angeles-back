package com.clubatletismolosangeles.losangeleswebback.service;

import com.clubatletismolosangeles.losangeleswebback.dto.LoginResponse;
import com.clubatletismolosangeles.losangeleswebback.exception.InvalidCredentialsException;
import com.clubatletismolosangeles.losangeleswebback.exception.UserNotFoundException;
import com.clubatletismolosangeles.losangeleswebback.model.Role;
import com.clubatletismolosangeles.losangeleswebback.model.User;
import com.clubatletismolosangeles.losangeleswebback.repository.UserRepository;
import com.clubatletismolosangeles.losangeleswebback.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService; // Antes decía JWTUtil

    @Autowired
    private PasswordEncoder passwordEncoder; // Necesario para comparar contraseñas seguras

    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("El usuario '" + username + "' no existe"));

        // Usamos passwordEncoder en lugar de .equals()
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("La contraseña proporcionada es incorrecta");
        }

        // Convertimos el Set<Role> en List<String> para el JWT y la respuesta
        List<String> rolesNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String token = jwtService.generateToken(username, rolesNames);

        return new LoginResponse(token, rolesNames);
    }
}