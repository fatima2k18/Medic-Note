package com.example.MedicNote_Application.repository;

import com.example.MedicNote_Application.model.Doctor;
import com.example.MedicNote_Application.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
