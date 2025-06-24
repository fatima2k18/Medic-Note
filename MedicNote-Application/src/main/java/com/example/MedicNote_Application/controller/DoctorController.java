package com.example.MedicNote_Application.controller;

import com.example.MedicNote_Application.dto.DoctorLoginRequest;
import com.example.MedicNote_Application.model.Doctor;
import com.example.MedicNote_Application.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor/apis")
public class DoctorController {
    @Autowired
    private DoctorService doctorService; //  Injecting the service bean
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Doctor user) {
        String message = doctorService.registerUser(user);
        return ResponseEntity.ok(message);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctor));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.deleteDoctor(id));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginDoctor(@RequestBody DoctorLoginRequest request) {
        String response = doctorService.loginUser(request.getMailId(), request.getPassword());
        return ResponseEntity.ok(response);
    }


}
