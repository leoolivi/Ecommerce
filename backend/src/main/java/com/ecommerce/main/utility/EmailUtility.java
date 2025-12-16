package com.ecommerce.main.utility;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import jakarta.annotation.PostConstruct;

@Component
public class EmailUtility {
    
    @Value("${mailtrap.token}")
    private String token;
    
    @Value("${mailtrap.sandbox:false}")  // default: production
    private boolean sandbox;
    
    @Value("${mailtrap.inbox.id:#{null}}")
    private Long inboxId;  // Necessario SOLO per sandbox
    
    private MailtrapClient client;
    
    @PostConstruct
    public void init() {
        MailtrapConfig.Builder configBuilder = new MailtrapConfig.Builder()
            .token(token);
        
        if (sandbox) {
            // Modalità SANDBOX - email NON inviate
            if (inboxId == null) {
                throw new IllegalStateException("Inbox ID required for sandbox mode");
            }
            configBuilder.sandbox(true).inboxId(inboxId);
        }
        // Se sandbox = false, è automaticamente in modalità PRODUCTION
        
        this.client = MailtrapClientFactory.createMailtrapClient(configBuilder.build());
    }
    
    public void sendEmail(String to, String subject, String text) {
        final MailtrapMail mail = MailtrapMail.builder()
            .from(new Address("hello@demomailtrap.co", "Mailtrap Test"))
            .to(List.of(new Address(to)))
            .subject(subject)
            .text(text)
            .build();
        
        try {
            System.out.println(client.send(mail));
        } catch (Exception e) {
            System.out.println("Caught exception: " + e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}