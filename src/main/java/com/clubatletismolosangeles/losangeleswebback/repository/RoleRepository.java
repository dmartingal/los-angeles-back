package com.clubatletismolosangeles.losangeleswebback.repository;

import com.clubatletismolosangeles.losangeleswebback.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}