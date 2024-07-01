package com.example.backend.services.user;

import com.example.backend.utils.EmailUtils;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    public static final String VERIFY_ACCOUNT = "Verify Account";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "emailTemplate";
    public static final String VIETNAMESE_FEET = "Vietnamese Feet";
    public static final String CONTENT_TYPE_ENCONDING = "text/html";
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender emailSender;
    private final ResourceLoader resourceLoader;
    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject(VERIFY_ACCOUNT);
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setText(EmailUtils.getEmailMessage(name, host, token));
            emailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageAttachments(String name, String to, String token) {
        try {
            MimeMessage mimeMessage = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(VERIFY_ACCOUNT);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(EmailUtils.getEmailMessage(name, host, token));
            //Add attachments
            // Load resources using ResourceLoader
            Resource resourceFrist = resourceLoader.getResource("classpath:static/image_test_send_mail/image_1.jpg");
            Resource resourceSecond = resourceLoader.getResource("classpath:static/image_test_send_mail/image_2.jpg");
//            Resource resourceThird = resourceLoader.getResource("classpath:static/plan.pdf");

            // Convert Resource to File
            File fileFirst = resourceFrist.getFile();
            File fileSecond = resourceSecond.getFile();
//            File fileThird = resourceThird.getFile();

            FileSystemResource fileSystemResourceFirst = new FileSystemResource(fileFirst);
            FileSystemResource fileSystemResourceSecond = new FileSystemResource(fileSecond);
//            FileSystemResource fileSystemResourceThird = new FileSystemResource(fileThird);

            helper.addAttachment(fileSystemResourceFirst.getFilename(),
                    fileSystemResourceFirst);
            helper.addAttachment(fileSystemResourceSecond.getFilename(),
                    fileSystemResourceSecond);
//            helper.addAttachment(fileSystemResourceThird.getFilename(),
//                    fileSystemResourceThird);

            emailSender.send(mimeMessage);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageEmbeddedImages(String name, String to, String token) {
        try {
            MimeMessage mimeMessage = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(VERIFY_ACCOUNT);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(EmailUtils.getEmailMessage(name, host, token));
            //Add attachments
            // Load resources using ResourceLoader
            Resource resourceFrist = resourceLoader.getResource("classpath:static/image_test_send_mail/image_1.jpg");
            Resource resourceSecond = resourceLoader.getResource("classpath:static/image_test_send_mail/image_2.jpg");
            Resource resourceThird = resourceLoader.getResource("classpath:static/plan.pdf");

            // Convert Resource to File
            File fileFirst = resourceFrist.getFile();
            File fileSecond = resourceSecond.getFile();
            File fileThird = resourceThird.getFile();

            FileSystemResource fileSystemResourceFirst = new FileSystemResource(fileFirst);
            FileSystemResource fileSystemResourceSecond = new FileSystemResource(fileSecond);
            FileSystemResource fileSystemResourceThird = new FileSystemResource(fileThird);

            helper.addInline(getContentId(fileSystemResourceFirst.getFilename()),
                    fileSystemResourceFirst);
            helper.addInline(getContentId(fileSystemResourceSecond.getFilename()),
                    fileSystemResourceSecond);
            helper.addInline(getContentId(fileSystemResourceThird.getFilename()),
                    fileSystemResourceThird);

            emailSender.send(mimeMessage);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }


    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {

        try {
            Context context = new Context();
            context.setVariables(Map.of("name", name, "url", EmailUtils.getVerificationUrl(host, token), "nameShop", VIETNAMESE_FEET));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);
            MimeMessage mimeMessage = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(VERIFY_ACCOUNT);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);

            emailSender.send(mimeMessage);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage mimeMessage = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(VERIFY_ACCOUNT);
            helper.setFrom(fromEmail);
            helper.setTo(to);

            Context context = new Context();
            context.setVariables(Map.of("name", name, "url", EmailUtils.getVerificationUrl(host, token), "nameShop", VIETNAMESE_FEET));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            // Add HTML email body
            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, CONTENT_TYPE_ENCONDING);
            mimeMultipart.addBodyPart(messageBodyPart);

//            // Add image to the email body
//            Resource resourceFrist = resourceLoader.getResource("classpath:static/image_test_send_mail/image_3.jpg");
//            File fileFirst = resourceFrist.getFile();
//            BodyPart imageBodyPart = new MimeBodyPart();
//            DataSource dataSource = new FileDataSource(fileFirst);
//            imageBodyPart.setDataHandler(new DataHandler(dataSource));
//            imageBodyPart.setHeader("Content-ID", "image");
//            mimeMultipart.addBodyPart(imageBodyPart);

            mimeMessage.setContent(mimeMultipart);

            emailSender.send(mimeMessage);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }


    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

    private String getContentId(String filename) {
        return "<" + filename + ">";
    }
}
