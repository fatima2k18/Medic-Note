package com.example.MedicNote_Application.service;
import com.example.MedicNote_Application.exception.ResourceNotFoundException;
import com.example.MedicNote_Application.model.Doctor;
import com.example.MedicNote_Application.model.Patient;
import com.example.MedicNote_Application.model.Prescription;
import com.example.MedicNote_Application.repository.DoctorRepository;
import com.example.MedicNote_Application.repository.PatientRepository;
import com.example.MedicNote_Application.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
@Service
public class PrescriptionService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    public Prescription savePrescription(Prescription prescription, Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        prescription.setDoctor(doctor);
        prescription.setPatient(patient);

        return prescriptionRepository.save(prescription);
    }
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    public Prescription updatePrescription(Long id, Prescription updatedPrescription) {
        Prescription existing = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        existing.setDiagnosis(updatedPrescription.getDiagnosis());
        existing.setMedications(updatedPrescription.getMedications());
        existing.setNotes(updatedPrescription.getNotes());
        existing.setIssuedDate(updatedPrescription.getIssuedDate());
        existing.setDoctor(updatedPrescription.getDoctor());
        existing.setPatient(updatedPrescription.getPatient());

        // Fetch full Doctor and Patient entities using only IDs from JSON
        Doctor doctor = doctorRepository.findById(updatedPrescription.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Patient patient = patientRepository.findById(updatedPrescription.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        existing.setDoctor(doctor);
        existing.setPatient(patient);

        return prescriptionRepository.save(existing);
    }
    public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }
    public Prescription patchPrescription(Long id, Prescription partialPrescription) {
        Prescription existing = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        if (partialPrescription.getDiagnosis() != null)
            existing.setDiagnosis(partialPrescription.getDiagnosis());

        if (partialPrescription.getMedications() != null)
            existing.setMedications(partialPrescription.getMedications());

        if (partialPrescription.getNotes() != null)
            existing.setNotes(partialPrescription.getNotes());

        if (partialPrescription.getIssuedDate() != null)
            existing.setIssuedDate(partialPrescription.getIssuedDate());

        if (partialPrescription.getDoctor() != null && partialPrescription.getDoctor().getId() != null) {
            Doctor doctor = doctorRepository.findById(partialPrescription.getDoctor().getId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            existing.setDoctor(doctor);
        }

        if (partialPrescription.getPatient() != null && partialPrescription.getPatient().getId() != null) {
            Patient patient = patientRepository.findById(partialPrescription.getPatient().getId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            existing.setPatient(patient);
        }

        return prescriptionRepository.save(existing);
    }


    public void deletePrescription(Long id) {

        prescriptionRepository.deleteById(id);
    }

    public List<Prescription> getPrescriptionsByDoctorId(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }
}
