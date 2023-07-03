package com.starlinex.controller;

import com.starlinex.entity.User;
import com.starlinex.model.*;
import com.starlinex.repository.UserRepository;
import com.starlinex.service.impl.AuthenticationService;
import com.starlinex.service.impl.TempUserService;
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
    private final TempUserService tempUserService;


    @PostMapping("/authenticate")
    public ResponseEntity<ServiceResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws Exception {
        ServiceResponse serviceResponse = new ServiceResponse();
        try{
            AuthenticationResponse authenticationResponse = service.authenticate(request);
            if(authenticationResponse != null){
                serviceResponse.setResponse(authenticationResponse);
                serviceResponse.setResponseCode(200);
                serviceResponse.setMessage("success");
            }else{
                serviceResponse.setResponse(null);
                serviceResponse.setResponseCode(400);
                serviceResponse.setMessage("Bad credentials");
            }

        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ServiceResponse> register(
            @RequestBody RegisterRequest request
    ) throws Exception {
        ServiceResponse serviceResponse = new ServiceResponse();
        try{
            Otp otp = tempUserService.register(request);
            if(otp!=null){
                serviceResponse.setResponse(otp);
                serviceResponse.setResponseCode(200);
                serviceResponse.setMessage("success");
            }
        else{
                serviceResponse.setResponse(null);
                serviceResponse.setResponseCode(404);
                serviceResponse.setMessage("Bad request");
            }

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ServiceResponse> verifyOtp(
            @RequestBody OtpId otpId
    ) throws Exception {
        ServiceResponse serviceResponse = new ServiceResponse();
        try{
            AuthenticationResponse authenticationResponse = tempUserService.verifyOtpAndSaveUser(otpId);
            if(authenticationResponse != null){
                serviceResponse.setResponse(authenticationResponse);
                serviceResponse.setResponseCode(200);
                serviceResponse.setMessage("success");
            }else{
                serviceResponse.setResponse(null);
                serviceResponse.setResponseCode(400);
                serviceResponse.setMessage("otp doesn't match");
            }

        }catch (Exception e){
            throw new Exception(e.getMessage());
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
    public ResponseEntity<Otp> getOtp(@RequestBody com.starlinex.model.User email){
        Optional<User> user = repository.findByEmail(email.getEmail());
        User use = user.get();
        Otp otp = new Otp();
        otp.setOtp(7896);
        otp.setId(use.getId());
        return ResponseEntity.ok(otp);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ServiceResponse> checkOtpAndUpdatePassword(@RequestBody OtpId otpId){
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setMessage("Success");
        serviceResponse.setResponseCode(200);
        serviceResponse.setResponse(service.forgetPassword(otpId));
        return ResponseEntity.ok(serviceResponse);
    }

}
