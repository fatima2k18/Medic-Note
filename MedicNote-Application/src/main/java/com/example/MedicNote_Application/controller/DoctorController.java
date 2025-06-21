package com.example.MedicNote_Application.controller;

import com.example.MedicNote_Application.model.Doctor;
import com.example.MedicNote_Application.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctor/apis")
public class DoctorController {
    @Autowired
    private DoctorService userService; //  Injecting the service bean
    @PostMapping("/register")
    public String registerUser(@RequestBody Doctor user){
        return userService.registerUser(user); // controller calls service
    }
    @PostMapping("/login")
    public String loginUser(@RequestBody Doctor user) {
        return userService.loginUser(user.getMailId(), user.getPassword());
    }

}
