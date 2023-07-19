package com.starlinex.service;

public interface EmailService {
    String sendOtp(String toEmail, String userName, String otp) throws Exception;
}
