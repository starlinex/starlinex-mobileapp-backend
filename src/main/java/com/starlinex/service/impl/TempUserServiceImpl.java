package com.starlinex.service.impl;

import com.starlinex.config.JwtService;
import com.starlinex.entity.TempUser;
import com.starlinex.entity.Token;
import com.starlinex.entity.TokenType;
import com.starlinex.entity.User;
import com.starlinex.exception.StarLinexException;
import com.starlinex.model.*;
import com.starlinex.repository.TempUserRepository;
import com.starlinex.repository.TokenRepository;
import com.starlinex.repository.UserRepository;
import com.starlinex.service.TempUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TempUserServiceImpl implements TempUserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TempUserServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final TempUserRepository repository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final EmailServiceImpl emailService;


    @Override
    public EmailMsg register(RegisterRequest request) throws StarLinexException {
        EmailMsg emailMsg = new EmailMsg();
        try{
            Optional<User> byEmail = userRepository.findByEmail(request.getEmail());
            if(byEmail.isPresent()){
                throw new StarLinexException("email already exists");
            }
            Random random = new Random();
            Integer generateOtp = random.nextInt(1010,10000);
        var user = TempUser
                .builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .otp(String.valueOf(generateOtp))
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedUser = repository.save(user);
        String msg = emailService.sendOtp(request.getEmail(),request.getName(), String.valueOf(generateOtp));
        emailMsg.setMsg(Objects.requireNonNullElse(msg, "Otp not sent"));
        emailMsg.setId(user.getId());
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            throw new StarLinexException(e.getMessage());
        }
        return emailMsg;
    }


    @Override
    public AuthenticationResponse verifyOtpAndSaveUser(SaveUser saveUser) throws StarLinexException {
        try{
            Optional<TempUser> tempUser = repository.findById(saveUser.getId());
            if(tempUser.isPresent()) {
                String otp = tempUser.get().getOtp();
                String reqOtp = String.valueOf(saveUser.getOtp());
                if (otp.equals(reqOtp)) {
                        var user = User.builder()
                                .name(tempUser.get().getName())
                                .email(tempUser.get().getEmail())
                                .phone(tempUser.get().getPhone())
                                .password(tempUser.get().getPassword())
                                .build();
                        var savedUser = userRepository.save(user);
                        repository.deleteById(saveUser.getId());
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
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            throw new StarLinexException(e.getMessage());
        }
        return null;
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
}
