package com.example.MedicNote_Application.model;

import com.example.MedicNote_Application.model.Patient;
import com.example.MedicNote_Application.service.DoctorService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Doctor is required")
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull(message = "Patient is required")
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotBlank(message = "Diagnosis is required")
    @Column(nullable = false)
    private String diagnosis;

    @NotBlank(message = "Medications are required")
    @Column(length = 1000, nullable = false)
    private String medications;

    @Column(length = 1000)
    private String notes;

    @Column(name = "issued_date")
    private LocalDate issuedDate;

    @PrePersist
    public void onCreate() {
        this.issuedDate = LocalDate.now();
    }
    @Column(nullable = false)
    private boolean isActive = true;

    // Getter
    public boolean isActive() {
        return isActive;
    }

    // Setter
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }


}
