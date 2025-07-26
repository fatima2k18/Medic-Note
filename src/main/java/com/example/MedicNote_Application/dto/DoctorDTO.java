package com.example.MedicNote_Application.dto;
import lombok.Data;
@Data
public class DoctorDTO {
    private Long id;
    private String name;
    private String specialization;
    private String phoneNo;   // Optional: add if you want to display contact
    private String mailId;    // Optional: show for booking or email contact
}
