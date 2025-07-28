package com.example.MedicNote_Application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    @Column(nullable = false, unique = true)
    private String phoneNo;

    @Min(value = 0, message = "Age must be positive")
    @Max(value = 120, message = "Age must be realistic")
    private int age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Prescription> prescriptions;
    @Column(nullable = false)
    private boolean isActive = true;
    public boolean getIsActive() {
        return isActive;
    }
    // ✅ Correct setter
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }


}
