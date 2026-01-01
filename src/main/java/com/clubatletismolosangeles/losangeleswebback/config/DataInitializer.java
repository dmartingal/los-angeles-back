package com.clubatletismolosangeles.losangeleswebback.config;

import com.clubatletismolosangeles.losangeleswebback.model.Role;
import com.clubatletismolosangeles.losangeleswebback.model.User;
import com.clubatletismolosangeles.losangeleswebback.repository.RoleRepository;
import com.clubatletismolosangeles.losangeleswebback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Crear Roles si no existen
        if (roleRepository.findByName("ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("USER") == null) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        // 2. Crear Usuario Admin por defecto
        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN");

            User admin = new User();
            admin.setUsername("admin");
            // Usamos el encoder (aunque ahora sea NoOp, es buena práctica)
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
            System.out.println("Usuario 'admin' creado con contraseña 'admin123'");
        }
    }
}