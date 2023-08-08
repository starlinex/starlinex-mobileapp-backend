package com.starlinex.service.impl;

import com.starlinex.exception.StarLinexException;
import com.starlinex.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender javaMailSender;

    @Override
    public String sendOtp(String toEmail, String userName, String otp) throws StarLinexException {
            String msg = null;
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            String msgBody = "Hi " + userName + "\n\n Your One Time Password(OTP) is " + otp + " the otp will expire in ten minutes if not used \n \n Thank You, \n Star Linex Team";
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setSubject("One Time Password (OTP) to verify Account");
            simpleMailMessage.setText(msgBody);
            javaMailSender.send(simpleMailMessage);
            msg = "Otp sent successfully \n" + "OTP: " + otp;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            throw new StarLinexException("Something went wrong");
        }
        return msg;
    }
}
