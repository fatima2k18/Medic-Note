package com.example.MedicNote_Application.controller;

import com.example.MedicNote_Application.model.Patient;
import com.example.MedicNote_Application.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient/apis")
public class PatientController {

        @Autowired
        private PatientService patientService;

        @PostMapping("/register")
        public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
            return ResponseEntity.ok(patientService.createPatient(patient));
        }

//        @GetMapping("/all")
//        public ResponseEntity<List<Patient>> getAllPatients() {
//            return ResponseEntity.ok(patientService.getAllPatients());
//        }
// ✅ Only ADMIN can view all patients
@GetMapping("/all")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<List<Patient>> getAllPatients() {
    return ResponseEntity.ok(patientService.getAllPatients());
}

//        @GetMapping("/{id}")
//        public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
//            return patientService.getPatientById(id)
//                    .map(ResponseEntity::ok)
//                    .orElse(ResponseEntity.notFound().build());
//        }
// ✅ Only ADMIN can view a specific patient
//@GetMapping("/active/{id}")
//@PreAuthorize("hasRole('ADMIN')")
//public ResponseEntity<Patient> getActivePatient(@PathVariable Long id) {
//    return patientService.getActivePatientById(id)
//            .map(ResponseEntity::ok)
//            .orElse(ResponseEntity.notFound().build());}
@GetMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")  // OR hasRole('PATIENT') with ownership check
public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
    return patientService.getActivePatientById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}


//        @PutMapping("/{id}")
//        public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
//            return ResponseEntity.ok(patientService.updatePatient(id, patient));
//        }
// ✅ Patient can update their own profile
@PutMapping("/{id}")
@PreAuthorize("hasRole('PATIENT')")
public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient, Authentication authentication) {
    String email = authentication.getName(); // from token
    return ResponseEntity.ok(patientService.updatePatient(id, patient, email));
}

//        @DeleteMapping("/{id}")
//        public ResponseEntity<String> deletePatient(@PathVariable Long id) {
//            patientService.deletePatient(id);
//            return ResponseEntity.ok("Patient deleted successfully.");
//        }
// ✅ Only ADMIN can delete a patient
@DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<String> deletePatient(@PathVariable Long id) {
    patientService.deletePatient(id);
    return ResponseEntity.ok("Patient deleted successfully.");
}
    //Get profile of patient
//    @GetMapping("/me")
//    @PreAuthorize("hasRole('PATIENT')")
//    public ResponseEntity<Patient> getPatientProfile(Authentication authentication) {
//        String email = authentication.getName(); // fetched from token
//        Patient patient = patientService.getPatientByEmail(email);
//        return ResponseEntity.ok(patient);
//    }
    // ✅ Patient can get their own profile
    @GetMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Patient> getPatientProfile(Authentication authentication) {
        String email = authentication.getName();
        Patient patient = patientService.getPatientByEmail(email);
        return ResponseEntity.ok(patient);
    }
}
