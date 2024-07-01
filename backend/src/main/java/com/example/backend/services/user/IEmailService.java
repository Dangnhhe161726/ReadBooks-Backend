package com.example.backend.services.user;

public interface IEmailService {
    void sendSimpleMailMessage(String name, String to, String token);
    void sendMimeMessageAttachments(String name, String to, String token);
    void sendMimeMessageEmbeddedImages(String name, String to, String token);
    void sendHtmlEmail(String name, String to, String token);
    void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token);
}
