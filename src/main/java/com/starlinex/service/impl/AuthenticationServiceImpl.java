package com.starlinex.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starlinex.config.JwtService;
import com.starlinex.entity.ForgetPassword;
import com.starlinex.entity.Token;
import com.starlinex.entity.TokenType;
import com.starlinex.entity.User;
import com.starlinex.exception.StarLinexException;
import com.starlinex.model.*;
import com.starlinex.repository.ForgetPasswordRepository;
import com.starlinex.repository.TokenRepository;
import com.starlinex.repository.UserRepository;
import com.starlinex.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailServiceImpl emailService;
    private final ForgetPasswordRepository forgetPasswordRepository;


    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        var id = user.getId();
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .id(id)
                .build();
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        var id = user.getId();
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .id(id)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }



    @Override
    @Transactional
    public String resetPassword(OtpId otpId) throws StarLinexException{
        try {
            Optional<ForgetPassword> forgetPassword = forgetPasswordRepository.findByUserId(otpId.getId());
            if (forgetPassword.isPresent()) {
                if (otpId.getOtp().equals(forgetPassword.get().getOtp())) {
                    User user = repository.findById(otpId.getId()).orElseThrow(()->new StarLinexException("User not found"));
                        user.setPassword(passwordEncoder.encode(otpId.getPassword()));
                        repository.save(user);
                        LOGGER.info("Password changed successfully");
                        forgetPasswordRepository.removeByUserId(user.getId());
                        return "Password changed successfully";
                }
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            throw new StarLinexException("Oops something went wrong");
        }
            return null;
    }

    @Override
    public EmailMsg sendOtpForForgetPassword(String email) throws StarLinexException {
        EmailMsg emailMsg = new EmailMsg();
        try {
            Random random = new Random();
            Integer generateOtp = random.nextInt(1010,9999);
            Optional<User> user = repository.findByEmail(email);
            if (user.isPresent()) {
                var forgetPassword = ForgetPassword.builder()
                        .otp(generateOtp)
                        .userId(user.get().getId())
                        .build();
                forgetPasswordRepository.save(forgetPassword);
                String msg = emailService.sendOtp(email, user.get().getName(), String.valueOf(generateOtp));
                emailMsg.setMsg(msg);
                emailMsg.setOtp(generateOtp);
                emailMsg.setId(user.get().getId());
            }else{
                throw new StarLinexException("email doesn't exist");
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            throw new StarLinexException("Something went wrong");
        }
        return emailMsg;
    }
}
