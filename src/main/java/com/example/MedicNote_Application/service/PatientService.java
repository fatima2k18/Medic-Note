package com.example.MedicNote_Application.service;

import com.example.MedicNote_Application.exception.ResourceNotFoundException;
import com.example.MedicNote_Application.model.Patient;
import com.example.MedicNote_Application.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException; // ✅ RIGHT
@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    // ✅ Public registration
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }
    public Optional<Patient> getActivePatientById(Long id) {  // ✅ Correct
        return patientRepository.findByIdAndIsActiveTrue(id);
    }
    // ✅ Admin only: view all active patients
    public List<Patient> getAllPatients() {
     //   return patientRepository.findByIsActiveTrue(); // soft delete-aware
        return patientRepository.findAll(); // ✅ shows all patients, even if isActive = false
    }

    // ✅ Admin only: get patient by ID
//    public Optional<Patient> getPatientById(Long id) {
//        return patientRepository.findById(id)
//                .filter(Patient::isActive); // ignore deleted ones
//    }
    // ✅ Patient can update their own data only
    public Patient updatePatient(Long id, Patient updatedPatient, String emailFromToken) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        if (!patient.getEmail().equals(emailFromToken)) {
            throw new AccessDeniedException("You can only update your own profile.");
        }

        patient.setName(updatedPatient.getName());
        patient.setPhoneNo(updatedPatient.getPhoneNo());
        patient.setAge(updatedPatient.getAge());
        patient.setGender(updatedPatient.getGender());
        return patientRepository.save(patient);
    }

    // ✅ Patient: get self by email
    public Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with email: " + email));
    }

    // ✅ Admin only: soft delete
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        patient.setIsActive(false); // soft delete
        patientRepository.save(patient);
    }
}
