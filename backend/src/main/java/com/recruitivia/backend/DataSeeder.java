package com.recruitivia.backend;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.recruitivia.backend.entity.Role;
import com.recruitivia.backend.repository.RoleRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        for (String name : List.of("ADMIN", "EMPLOYER", "APPLICANT")) {
            if (roleRepository.findByName(name).isEmpty()) {
                roleRepository.save(new Role(name));
            }
        }
    }
}