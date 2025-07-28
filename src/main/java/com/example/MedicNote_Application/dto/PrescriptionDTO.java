package com.example.MedicNote_Application.dto;
import lombok.Data;
@Data
public class PrescriptionDTO {
    private String diagnosis;
    private String medications;
    private String issuedDate; // Prefer String for input; will convert to LocalDate in service
    private String notes;
    private Long doctorId;
    private Long patientId;
}
