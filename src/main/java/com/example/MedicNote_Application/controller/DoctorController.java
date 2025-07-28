package com.example.MedicNote_Application.controller;

import com.example.MedicNote_Application.dto.DoctorDTO;
import com.example.MedicNote_Application.exception.ResourceNotFoundException;
import com.example.MedicNote_Application.model.Doctor;
import com.example.MedicNote_Application.model.Prescription;
import com.example.MedicNote_Application.repository.DoctorRepository;
import com.example.MedicNote_Application.service.DoctorService;
import com.example.MedicNote_Application.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/doctor/apis")
public class DoctorController {
    @Autowired
    private DoctorService doctorService; //  Injecting the service bean
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PrescriptionService prescriptionService;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Doctor user) {
        String message = doctorService.registerUser(user);
        return ResponseEntity.ok(message);
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }
    @GetMapping("/public/doctors")
    public ResponseEntity<List<DoctorDTO>> listDoctorsForPatients() {
        List<DoctorDTO> list = doctorService.getAllDoctorsForPatientView();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctor));
    }
//    @PreAuthorize("hasRole('DOCTOR')")
//    public ResponseEntity<String> updateDoctor(
//            @PathVariable Long id,
//            @RequestBody Doctor updatedDoctor,
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        String username = userDetails.getUsername(); // username is email
//        Doctor loggedInDoctor = doctorService.getDoctorByMailId(username);
//
//        // ✅ Check if doctor owns the profile
//        if (!loggedInDoctor.getId().equals(id)) {
//            return ResponseEntity.status(403).body("You are not allowed to update another doctor's profile");
//        }

//        // ✅ Delegate update logic
//        return ResponseEntity.ok(doctorService.updateDoctor(id, updatedDoctor));
//    }
//@PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
//        return ResponseEntity.ok(doctorService.deleteDoctor(id));
//    }
@DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
    Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

    doctor.setIsActive(false); // ✅ Soft delete
    doctorRepository.save(doctor);

    return ResponseEntity.ok("Doctor deactivated successfully");
}


//    @PostMapping("/login")
//    public ResponseEntity<String> loginDoctor(@RequestBody DoctorLoginRequest request) {
//        String response = doctorService.loginUser(request.getMailId(), request.getPassword());
//        return ResponseEntity.ok(response);
//    }
    // Secured: Get the profile of the currently authenticated doctor
    @GetMapping("/profile")
    public ResponseEntity<Doctor> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // Assuming username is email
        Doctor doctor = doctorService.getDoctorByMailId(email); // Implement this in service
        return ResponseEntity.ok(doctor);
    }
    //view own prescription
    @GetMapping("/{doctorId}/prescriptions")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<Prescription>> getDoctorPrescriptions(@PathVariable Long doctorId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctorId(doctorId);
        return ResponseEntity.ok(prescriptions);
    }

}
