package com.example.MedicNote_Application.service;

import com.example.MedicNote_Application.dto.NewUserDto;
import com.example.MedicNote_Application.model.User;
import com.example.MedicNote_Application.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.MedicNote_Application.model.Role;
import com.example.MedicNote_Application.dto.UpdateDto;
import com.example.MedicNote_Application.model.Role;
import com.example.MedicNote_Application.model.Role;


@Service
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder pwEncoder;

    @Autowired
    public UserService(UserRepository userRepo, PasswordEncoder pwEncoder) {
        this.userRepo = userRepo;
        this.pwEncoder = pwEncoder;
    }

    @Transactional
    public User register(NewUserDto dto) {
        if (userRepo.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already taken.");
        }

        User u = new User();
        u.setUsername(dto.getUsername());
        u.setPassword(pwEncoder.encode(dto.getPassword()));
        u.setRole(dto.getRole() != null ? dto.getRole() : Role.PATIENT); // Allow admin if passed

        return userRepo.save(u);
    }

    public User getById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void updateProfile(Long id, UpdateDto dto) {
        User user = getById(id);
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(pwEncoder.encode(dto.getPassword()));
        }
        // Other fields...
        userRepo.save(user);
    }
}

