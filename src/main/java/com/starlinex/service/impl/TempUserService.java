package com.starlinex.service.impl;

import com.starlinex.config.JwtService;
import com.starlinex.entity.TempUser;
import com.starlinex.entity.Token;
import com.starlinex.entity.TokenType;
import com.starlinex.entity.User;
import com.starlinex.model.AuthenticationResponse;
import com.starlinex.model.Otp;
import com.starlinex.model.OtpId;
import com.starlinex.model.RegisterRequest;
import com.starlinex.repository.TempUserRepository;
import com.starlinex.repository.TokenRepository;
import com.starlinex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TempUserService {

    private final PasswordEncoder passwordEncoder;
    private final TempUserRepository repository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    public Otp register(RegisterRequest request) throws Exception {
        Otp otp = new Otp();
        try{
        var user = TempUser
                .builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedUser = repository.save(user);
            System.out.println(savedUser);
            otp.setOtp(5483);
            otp.setId(user.getId());
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return otp;
    }

    public AuthenticationResponse verifyOtpAndSaveUser(OtpId otpId) throws Exception {
        try{
            if(otpId.getOtp().equals(5483) && otpId.getId() != null){
                Optional<TempUser> tempUser = repository.findById(otpId.getId());
                if(tempUser.isPresent()) {
                    var user = User.builder()
                            .name(tempUser.get().getName())
                            .email(tempUser.get().getEmail())
                            .phone(tempUser.get().getPhone())
                            .password(tempUser.get().getPassword())
                            .build();
                    var savedUser = userRepository.save(user);
                    repository.deleteById(otpId.getId());
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
            throw new Exception(e.getMessage());
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
