package com.example.MedicNote_Application.repository;

import com.example.MedicNote_Application.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findByMailId(String mailId); // use field name from your model

}
