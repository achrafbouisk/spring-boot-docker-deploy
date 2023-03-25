package com.ecomteam.shop_dddv3.infrastructure.services.mailing;

import org.springframework.mail.SimpleMailMessage;

public interface MailService {
    void sendMail(SimpleMailMessage email);
}
