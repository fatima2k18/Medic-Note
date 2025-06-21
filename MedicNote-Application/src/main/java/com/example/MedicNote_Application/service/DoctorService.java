package com.example.MedicNote_Application.service;

import com.example.MedicNote_Application.model.Doctor;
import com.example.MedicNote_Application.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class DoctorService {
    @Autowired
    private DoctorRepository userRepository;

    public  String registerUser(Doctor user) {
        Optional<Doctor> existingUser = userRepository.findByMailId(user.getMailId());
        if (existingUser.isPresent()) {
            return "Email already registered!";
        }
        userRepository.save(user);
        return "Registration successful!";
    }
    public String loginUser(String mailId, String password) {
        Optional<Doctor> existingUser = userRepository.findByMailId(mailId);
        if (existingUser.isPresent()) {
            if (existingUser.get().getPassword().equals(password)) {
                return "Login successful!";
            }
            return "Incorrect password!";
        }
        return "Email not found!";
    }
}
