package org.example.jee.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.example.jee.event.MemberRegisteredEvent;
import org.example.jee.model.Member;

@Stateless
public class Mail {
 
    @Inject
    private Logger log;

    @Resource(name = "java:jboss/mail/gmail")
    private Session session;
 
    public void send(@Observes MemberRegisteredEvent memberRegEvent) {
 
        try {        	
        	String template = "Congratulations, you are registered!\nPlease validate your email address with the"
            		+ " following URL:\n" + "http://localhost:8080/example-jee-user-registration-web/rest/members/%s/validate-emailaddress";

        	Member member = memberRegEvent.getMember();
        	
        	String textMessage = String.format(template, member.getId());
 
            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(member.getEmail()));
            message.setSubject("new user registered");
            message.setText(textMessage);
 
            Transport.send(message);
 
        } catch (MessagingException e) {
        	log.log(Level.WARNING, "Cannot send mail", e);
        }
    }
}