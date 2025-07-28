package com.example.MedicNote_Application.controller;

import com.example.MedicNote_Application.exception.ResourceNotFoundException;
import com.example.MedicNote_Application.model.Doctor;
import com.example.MedicNote_Application.model.Patient;
import com.example.MedicNote_Application.model.Prescription;

import com.example.MedicNote_Application.service.DoctorService;
import com.example.MedicNote_Application.service.PatientService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.MedicNote_Application.dto.PrescriptionDTO;
import com.example.MedicNote_Application.service.PrescriptionService;
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
    @Autowired
    private PatientService patientService; // ✅ this is the missing piece!
    @Autowired
    private DoctorService doctorService;

    @PostMapping("/create")
    public ResponseEntity<Prescription> create(@RequestBody PrescriptionDTO dto) {
        Prescription prescription = new Prescription();
        prescription.setDiagnosis(dto.getDiagnosis());
        prescription.setMedications(dto.getMedications());
        prescription.setNotes(dto.getNotes());
        // prescription.setIssuedDate(LocalDate.parse(dto.getIssuedDate()));
        prescription.setIssuedDate(LocalDate.now()); // Automatically set the date

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

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN', 'DOCTOR')")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(
            @PathVariable Long patientId,
            Authentication authentication) {

        String emailFromToken = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        Patient patient = patientService.getActivePatientById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        if (!isAdmin && !patient.getEmail().equals(emailFromToken)) {
            throw new AccessDeniedException("You can only view your own prescriptions.");
        }

        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
        return ResponseEntity.ok(prescriptions);
    }


    // ✅ Endpoint: View all prescriptions by doctor

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<Prescription>> getPrescriptionsByDoctor(
            @PathVariable Long doctorId,
            Authentication authentication) {

        String emailFromToken = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        Doctor doctor = doctorService.getDoctorById(doctorId); // ✅ FIXED

        if (!isAdmin && !doctor.getMailId().equals(emailFromToken)) {
            throw new AccessDeniedException("You can only view your own prescriptions.");
        }

        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctorId(doctorId);
        return ResponseEntity.ok(prescriptions);
    }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> delete(@PathVariable Long id) {
            prescriptionService.deletePrescription(id);
            return ResponseEntity.ok("Prescription deleted successfully.");
        }
    }
