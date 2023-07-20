package com.starlinex.controller;

import com.starlinex.exception.StarLinexException;
import com.starlinex.model.*;
import com.starlinex.service.impl.AuthenticationServiceImpl;
import com.starlinex.service.impl.TempUserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl service;
    private final TempUserServiceImpl tempUserService;


    @PostMapping("/authenticate")
    public ResponseEntity<ServiceResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws StarLinexException {
        ServiceResponse serviceResponse = new ServiceResponse();
        try {
            AuthenticationResponse authenticationResponse = service.authenticate(request);
            serviceResponse.setResponse(authenticationResponse);
            serviceResponse.setResponseCode(200);
            serviceResponse.setMessage("success");
        } catch (Exception e) {
            throw new StarLinexException("Please check your credentials");
        }
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ServiceResponse> register(
            @RequestBody RegisterRequest request
    ) throws StarLinexException {
        ServiceResponse serviceResponse = new ServiceResponse();
        EmailMsg emailMsg = tempUserService.register(request);
        serviceResponse.setResponse(emailMsg);
        serviceResponse.setResponseCode(200);
        serviceResponse.setMessage("success");
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ServiceResponse> verifyOtp(
            @RequestBody OtpId otpId
    ) throws StarLinexException {
        ServiceResponse serviceResponse = new ServiceResponse();
        try {
            AuthenticationResponse authenticationResponse = tempUserService.verifyOtpAndSaveUser(otpId);
            if (authenticationResponse != null) {
                serviceResponse.setResponse(authenticationResponse);
                serviceResponse.setResponseCode(200);
                serviceResponse.setMessage("success");
            } else {
                serviceResponse.setResponse(null);
                serviceResponse.setResponseCode(200);
                serviceResponse.setMessage("otp doesn't match");
            }

        } catch (Exception e) {
            throw new StarLinexException("Something went wrong");
        }

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
    public ResponseEntity<ServiceResponse> getOtp(@RequestBody com.starlinex.model.User email) throws StarLinexException {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setMessage("Success");
        serviceResponse.setResponseCode(200);
        serviceResponse.setResponse(service.sendOtpForForgetPassword(email.getEmail()));
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ServiceResponse> checkOtpAndUpdatePassword(@RequestBody OtpId otpId) {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setMessage("Success");
        serviceResponse.setResponseCode(200);
        serviceResponse.setResponse(service.resetPassword(otpId));
        return ResponseEntity.ok(serviceResponse);
    }

}
