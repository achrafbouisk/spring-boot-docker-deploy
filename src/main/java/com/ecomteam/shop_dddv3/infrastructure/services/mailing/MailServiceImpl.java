package com.ecomteam.shop_dddv3.infrastructure.services.mailing;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{

    private final JavaMailSender javaMailSender;

    @Async
    public void sendMail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
}
