package com.example.MedicNote_Application.controller;

import com.example.MedicNote_Application.model.Doctor;
import com.example.MedicNote_Application.model.Patient;
import com.example.MedicNote_Application.model.Prescription;
import com.example.MedicNote_Application.service.PrescriptionService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.MedicNote_Application.dto.PrescriptionDTO;
import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/create")
    public ResponseEntity<Prescription> create(@RequestBody PrescriptionDTO dto) {
        Prescription prescription = new Prescription();
        prescription.setDiagnosis(dto.getDiagnosis());
        prescription.setMedications(dto.getMedications());
        prescription.setNotes(dto.getNotes());
        prescription.setIssuedDate(LocalDate.parse(dto.getIssuedDate()));

        Prescription saved = prescriptionService.savePrescription(prescription, dto.getDoctorId(), dto.getPatientId());
        return ResponseEntity.ok(saved); //
    }

    @GetMapping("/all")
    public ResponseEntity<List<Prescription>> getAll() {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        return prescriptionService.getPrescriptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> update(@PathVariable Long id, @RequestBody Prescription prescription) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(id, prescription));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Prescription> patchPrescription(
            @PathVariable Long id,
            @RequestBody Prescription partialPrescription) {

        Prescription updated = prescriptionService.patchPrescription(id, partialPrescription);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.ok("Prescription deleted successfully.");
    }
}
