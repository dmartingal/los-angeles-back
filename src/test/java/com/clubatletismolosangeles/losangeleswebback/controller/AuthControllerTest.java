package com.clubatletismolosangeles.losangeleswebback.controller;

import com.clubatletismolosangeles.losangeleswebback.dto.LoginRequest;
import com.clubatletismolosangeles.losangeleswebback.model.Role;
import com.clubatletismolosangeles.losangeleswebback.model.User;
import com.clubatletismolosangeles.losangeleswebback.repository.RoleRepository;
import com.clubatletismolosangeles.losangeleswebback.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
// Inyectamos la propiedad del secreto para que el test no falle al cargar el contexto
@TestPropertySource(properties = {
        "app.jwt.secret=ClaveSuperSecretaDeMasDe32CaracteresParaElTest123456",
        "app.jwt.expirationMinutes=180"
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // 1. Creamos el Rol
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);

        // 2. Creamos el Usuario con la contraseña encriptada
        User user = new User();
        user.setUsername("atleta_admin");
        user.setPassword(passwordEncoder.encode("password123")); // Encriptado real
        user.setRoles(Set.of(adminRole));
        userRepository.save(user);
    }

    @Test
    void loginWithValidUser_ShouldReturnTokenAndRoles() throws Exception {
        // Preparar la petición
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("atleta_admin");
        loginRequest.setPassword("password123");

        // Ejecutar y Verficar
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").exists()) // Verifica que devuelve el campo 'jwt'
                .andExpect(jsonPath("$.roles[0]").value("ADMIN")); // Verifica el primer rol
    }

    @Test
    void loginWithInvalidPassword_ShouldReturn401() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("atleta_admin");
        loginRequest.setPassword("clave_erronea");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized()) // Cambiado de 500 a 401
                .andExpect(result -> {
                    // Opcional: verificar el mensaje de error que configuramos
                    String message = result.getResponse().getContentAsString();
                    assert message.contains("La contraseña proporcionada es incorrecta");
                });
    }
}