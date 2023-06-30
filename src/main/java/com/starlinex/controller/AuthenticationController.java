package com.starlinex.controller;

import com.starlinex.entity.User;
import com.starlinex.model.*;
import com.starlinex.repository.UserRepository;
import com.starlinex.service.impl.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserRepository repository;

    @PostMapping("/register")
    public ResponseEntity<ServiceResponse> register(
            @RequestBody RegisterRequest request
    ) {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setResponse(service.register(request));
        serviceResponse.setResponseCode(200);
        serviceResponse.setMessage("success");
        return ResponseEntity.ok(serviceResponse);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<ServiceResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setResponse(service.authenticate(request));
        serviceResponse.setResponseCode(200);
        serviceResponse.setMessage("success");
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
    @PostMapping("/forgetPassword")
    public ResponseEntity<Otp> getOtp(@RequestBody com.starlinex.model.User email){
        Optional<User> user = repository.findByEmail(email.getEmail());
        User use = user.get();
        Otp otp = new Otp();
        otp.setOtp(7896);
        otp.setId(use.getId());
        return ResponseEntity.ok(otp);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ServiceResponse> checkOtpAndUpdatePassword(@RequestBody OtpId otpId){
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setMessage("Success");
        serviceResponse.setResponseCode(200);
        serviceResponse.setResponse(service.forgetPassword(otpId));
        return ResponseEntity.ok(serviceResponse);
    }

}
