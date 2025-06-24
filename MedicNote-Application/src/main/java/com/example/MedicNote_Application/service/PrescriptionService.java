package com.example.MedicNote_Application.service;

import com.example.MedicNote_Application.model.Prescription;
import com.example.MedicNote_Application.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public Prescription savePrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(Math.toIntExact(id));
    }

    public Prescription updatePrescription(Long id, Prescription updatedPrescription) {
        Prescription existing = prescriptionRepository.findById(Math.toIntExact(id)).orElseThrow();
        existing.setDiagnosis(updatedPrescription.getDiagnosis());
        existing.setMedications(updatedPrescription.getMedications());
        existing.setNotes(updatedPrescription.getNotes());
        existing.setIssuedDate(updatedPrescription.getIssuedDate());
        return prescriptionRepository.save(existing);
    }

    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(Math.toIntExact(id));
    }
}
