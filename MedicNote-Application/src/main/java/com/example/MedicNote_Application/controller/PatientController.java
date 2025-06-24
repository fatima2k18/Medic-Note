package com.example.MedicNote_Application.controller;

import com.example.MedicNote_Application.model.Patient;
import com.example.MedicNote_Application.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient/apis")
public class PatientController {

        @Autowired
        private PatientService patientService;

        @PostMapping("/create")
        public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
            return ResponseEntity.ok(patientService.createPatient(patient));
        }

        @GetMapping("/all")
        public ResponseEntity<List<Patient>> getAllPatients() {
            return ResponseEntity.ok(patientService.getAllPatients());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
            return patientService.getPatientById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @PutMapping("/{id}")
        public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
            return ResponseEntity.ok(patientService.updatePatient(id, patient));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> deletePatient(@PathVariable Long id) {
            patientService.deletePatient(id);
            return ResponseEntity.ok("Patient deleted successfully.");
        }
}
