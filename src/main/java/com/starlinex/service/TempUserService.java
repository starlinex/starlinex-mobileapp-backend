package com.starlinex.service;

import com.starlinex.model.AuthenticationResponse;
import com.starlinex.model.EmailMsg;
import com.starlinex.model.OtpId;
import com.starlinex.model.RegisterRequest;

public interface TempUserService {
    EmailMsg register(RegisterRequest request) throws Exception;

    AuthenticationResponse verifyOtpAndSaveUser(OtpId otpId) throws Exception;
}
