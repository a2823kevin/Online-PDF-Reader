package com.a2823kevin.pdfreader.backend.config;

import java.io.File;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.a2823kevin.pdfreader.backend.model.Role;
import com.a2823kevin.pdfreader.backend.model.User;
import com.a2823kevin.pdfreader.backend.repository.RoleRepository;
import com.a2823kevin.pdfreader.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final AdminProperties adminProperties;
    private final FileSavingProperties fileSavingProperties;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        File folder = new File(fileSavingProperties.getPdfpath());
        if (!folder.exists()) {
            folder.mkdirs();
        }

        folder = new File(fileSavingProperties.getThumbnailpath());
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // add roles
        createRoleIfNotExists("USER");
        createRoleIfNotExists("ADMIN");

        // default admin
        if (!userRepository.findByUsername(adminProperties.getUsername()).isPresent()) {
            User admin = new User();
            Role role = roleRepository.findByName("ADMIN");
            admin.setUsername(adminProperties.getUsername());
            admin.setPassword(passwordEncoder.encode(adminProperties.getPassword()));
            admin.setRoles(List.of(role));
            userRepository.save(admin);
        }
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName)==null) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
