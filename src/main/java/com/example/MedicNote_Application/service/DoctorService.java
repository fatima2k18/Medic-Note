package com.example.MedicNote_Application.service;

import com.example.MedicNote_Application.dto.DoctorDTO;
import com.example.MedicNote_Application.exception.ResourceNotFoundException;
import com.example.MedicNote_Application.model.Doctor;
import com.example.MedicNote_Application.model.Prescription;
import com.example.MedicNote_Application.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import com.example.MedicNote_Application.repository.DoctorRepository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    // ✅ Register doctor profile (not for login/auth)
    // ✅ Register doctor profile (not for login/auth)
    public String registerUser(Doctor user) {
        Optional<Doctor> existingUser = doctorRepository.findByMailId(user.getMailId());
        if (existingUser.isPresent()) {
            return "Email already registered!";
        }
        doctorRepository.save(user);
        return "Registration successful!";
    }

    // ✅ Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // ✅ Get doctor by ID with access control
    public Doctor getDoctorById(Long id) {
        String emailFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // Only allow if the doctor is viewing their own profile or is an admin
        if (!doctor.getMailId().equals(emailFromToken) && !isAdmin) {
            throw new AccessDeniedException("Access denied: You can only view your own profile.");
        }

        return doctor;
    }

    // ✅ Update doctor by ID
    public String updateDoctor(Long id, Doctor updatedDoctor) {
        //String emailFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        // Only allow the doctor to update their own profile
//        if (!doctor.getMailId().equals(emailFromToken)) {
//            throw new AccessDeniedException("Access denied: You can only update your own profile.");
//        }
        //Doctor doctor = getDoctorById(id);
        doctor.setName(updatedDoctor.getName());
        doctor.setMailId(updatedDoctor.getMailId());
        doctor.setSpecialization(updatedDoctor.getSpecialization());
        doctorRepository.save(doctor);
        return "Doctor updated successfully!";
    }

    // ✅ Delete doctor by ID
    // ✅ Soft Delete Logic: Admin can deactivate doctor
    public String deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        doctor.setIsActive(false); // mark as deactivated
        doctorRepository.save(doctor);

        return "Doctor deactivated successfully!";
    }

    // ✅ Public view for patients: list all doctors
    public List<DoctorDTO> getAllDoctorsForPatientView() {
        List<Doctor> doctors = doctorRepository.findAll();

        return doctors.stream().map(doctor -> {
            DoctorDTO dto = new DoctorDTO();
            dto.setId(doctor.getId());
            dto.setName(doctor.getName());
            dto.setSpecialization(doctor.getSpecialization());
            dto.setPhoneNo(doctor.getPhoneNo());
            dto.setMailId(doctor.getMailId());
            return dto;
        }).collect(Collectors.toList());
    }

    // ✅ Get doctor by email
    public Doctor getDoctorByMailId(String email) {
        return doctorRepository.findByMailId(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found with email: " + email));
    }
}

