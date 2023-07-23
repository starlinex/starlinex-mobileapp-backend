package com.starlinex.service;

import com.starlinex.model.*;

public interface TempUserService {
    EmailMsg register(RegisterRequest request) throws Exception;

    AuthenticationResponse verifyOtpAndSaveUser(SaveUser otpId) throws Exception;
}
