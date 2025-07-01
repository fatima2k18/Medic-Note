package com.example.MedicNote_Application.model;

import jakarta.persistence.*;

@Entity // ✅ This annotation is missing, and it's essential!
@Table(name = "users") // Optional: to avoid conflicts with SQL reserved keyword "user"
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ Important!
    private Long id;

    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    private String password;
    @Enumerated(EnumType.STRING) // ➤ Tells JPA to store enum name as string like "DOCTOR"
    @Column(nullable = false)

    private Role role;
}
