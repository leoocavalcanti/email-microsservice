package com.ms.email.services;

import com.ms.email.dtos.EmailDTO;
import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import com.ms.email.repositories.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;

    private final JavaMailSender emailSender;

    public EmailDTO sendEmail(EmailDTO emailDto) {

        EmailModel email = new EmailModel();
        BeanUtils.copyProperties(emailDto, email);
        email.setSendDateEmail(LocalDateTime.now());

        try{

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email.getEmailFrom());
            message.setTo(email.getEmailTo());
            message.setSubject(email.getSubject());
            message.setText(email.getText());
            emailSender.send(message);
            email.setStatusEmail(StatusEmail.SENT);
            emailRepository.save(email);
            return new EmailDTO(email);
        }

        catch(MailException e){

            email.setStatusEmail(StatusEmail.ERROR);
            throw new MailSendException("Erro ao enviar email");

        }

    }
}
