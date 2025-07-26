package com.example.MedicNote_Application.repository;

import com.example.MedicNote_Application.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByDoctorId(Long doctorId);
    // ✅ Add this custom method for patient-based lookup
    List<Prescription> findByPatientId(Long patientId);
}
//    List<Prescription> findByDoctorId(Long doctorId);



