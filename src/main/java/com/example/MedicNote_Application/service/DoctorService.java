package com.example.MedicNote_Application.service;

import com.example.MedicNote_Application.model.Doctor;
import com.example.MedicNote_Application.model.Prescription;
import com.example.MedicNote_Application.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DoctorService {
    @Autowired
    private DoctorRepository  doctorRepository;

    public  String registerUser(Doctor user) {
        Optional<Doctor> existingUser = doctorRepository.findByMailId(user.getMailId());
        if (existingUser.isPresent()) {
            return "Email already registered!";
        }
        doctorRepository.save(user);
        return "Registration successful!";
    }
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }
    public String updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor doctor = getDoctorById(id);
        doctor.setName(updatedDoctor.getName());
        doctor.setMailId(updatedDoctor.getMailId());
        doctor.setPassword(updatedDoctor.getPassword());
        doctor.setSpecialization(updatedDoctor.getSpecialization());
        doctorRepository.save(doctor);
        return "Doctor updated successfully!";
    }
    public String deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
        return "Doctor deleted successfully!";
    }

    public String loginUser(String mailId, String password) {
        Optional<Doctor> existingUser = doctorRepository.findByMailId(mailId);
        if (existingUser.isPresent()) {
            if (existingUser.get().getPassword().equals(password)) {
                return "Login successful!";
            }
            return "Incorrect password!";
        }
        return "Email not found!";
    }
    public Doctor getDoctorByMailId(String email) {
        return doctorRepository.findByMailId(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found with email: " + email));
    }

}
