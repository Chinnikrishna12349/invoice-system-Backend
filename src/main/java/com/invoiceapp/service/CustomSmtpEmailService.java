package com.invoiceapp.service;

import com.invoiceapp.dto.InvoiceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@ConditionalOnProperty(name = "app.email.provider", havingValue = "smtp")
public class CustomSmtpEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(CustomSmtpEmailService.class);

    private final JavaMailSender javaMailSender;
    private final PdfService pdfService;

    @Value("${app.mail.from:${spring.mail.username:hr@visionai.jp}}")
    private String senderEmail;

    @Value("${app.mail.from-name:Vision AI}")
    private String senderName;

    public CustomSmtpEmailService(JavaMailSender javaMailSender, PdfService pdfService) {
        this.javaMailSender = javaMailSender;
        this.pdfService = pdfService;
        logger.info("CustomSmtpEmailService initialized. Using direct SMTP server.");
    }

    @Override
    public void sendInvoiceEmail(InvoiceDTO invoice) {
        sendInvoiceEmail(invoice, (List<String>) null);
    }

    @Override
    public void sendInvoiceEmail(InvoiceDTO invoice, List<String> additionalEmails) {
        byte[] pdfBytes = null;
        try {
            pdfBytes = pdfService.generateInvoicePdf(invoice);
        } catch (Exception e) {
            logger.warn("Could not generate PDF for SMTP email: {}", e.getMessage());
        }
        sendInvoiceEmailWithPdf(invoice, pdfBytes, additionalEmails);
    }

    @Override
    public void sendInvoiceEmail(InvoiceDTO invoice, String recipientEmail) {
        sendInvoiceEmailWithPdf(invoice, null, null);
    }

    @Override
    public void sendInvoiceEmailWithPdf(InvoiceDTO invoice, byte[] pdfBytes) {
        sendInvoiceEmailWithPdf(invoice, pdfBytes, null);
    }

    @Override
    public void sendInvoiceEmailWithPdf(InvoiceDTO invoice, byte[] pdfBytes, List<String> additionalEmails) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            String finalSender = resolveSenderEmail(invoice);
            helper.setFrom(new InternetAddress(finalSender, senderName));
            helper.setTo(invoice.getEmployeeEmail());

            if (additionalEmails != null) {
                for (String email : additionalEmails) {
                    if (StringUtils.hasText(email) && !email.equalsIgnoreCase(invoice.getEmployeeEmail())) {
                        helper.addCc(email.trim());
                    }
                }
            }

            String subject = "Invoice #" + invoice.getInvoiceNumber() + " - " + senderName;
            helper.setSubject(subject);

            String htmlContent = buildHtmlContent(invoice);
            helper.setText(htmlContent, true);

            if (pdfBytes != null && pdfBytes.length > 0) {
                String filename = "Invoice_" + invoice.getInvoiceNumber().replace("#", "").replace(" ", "_") + ".pdf";
                helper.addAttachment(filename, new ByteArrayResource(pdfBytes));
            }

            logger.info("Sending SMTP email directly from {} to {}", finalSender, invoice.getEmployeeEmail());
            javaMailSender.send(message);
            logger.info("Successfully sent SMTP email for Invoice #{}", invoice.getInvoiceNumber());

        } catch (Exception e) {
            logger.error("Failed to send direct SMTP email: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send email via SMTP server: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendInvoiceEmailWithMultiplePdfs(InvoiceDTO invoice, List<Attachment> attachments, List<String> additionalEmails) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            String finalSender = resolveSenderEmail(invoice);
            helper.setFrom(new InternetAddress(finalSender, senderName));
            helper.setTo(invoice.getEmployeeEmail());

            if (additionalEmails != null) {
                for (String email : additionalEmails) {
                    if (StringUtils.hasText(email) && !email.equalsIgnoreCase(invoice.getEmployeeEmail())) {
                        helper.addCc(email.trim());
                    }
                }
            }

            String subject = "Invoice #" + invoice.getInvoiceNumber() + " - " + senderName;
            helper.setSubject(subject);

            String htmlContent = buildHtmlContent(invoice);
            helper.setText(htmlContent, true);

            if (attachments != null) {
                for (Attachment att : attachments) {
                    byte[] bytes = Base64.getDecoder().decode(att.getContent());
                    helper.addAttachment(att.getName(), new ByteArrayResource(bytes));
                }
            }

            logger.info("Sending SMTP email with multiple PDFs from {} to {}", finalSender, invoice.getEmployeeEmail());
            javaMailSender.send(message);
            logger.info("Successfully sent SMTP email with multiple PDFs for Invoice #{}", invoice.getInvoiceNumber());

        } catch (Exception e) {
            logger.error("Failed to send direct SMTP email with attachments: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send email via SMTP server: " + e.getMessage(), e);
        }
    }

    private String resolveSenderEmail(InvoiceDTO invoice) {
        if (invoice.getCompany() != null && StringUtils.hasText(invoice.getCompany().getEmail())) {
            return invoice.getCompany().getEmail().trim();
        }
        return senderEmail;
    }

    private String buildHtmlContent(InvoiceDTO invoice) {
        return "<!DOCTYPE html>" +
               "<html><body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>" +
               "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 8px;'>" +
               "<h2 style='color: #1E3A8A; margin-top: 0;'>New Invoice Available</h2>" +
               "<p>Dear " + (StringUtils.hasText(invoice.getEmployeeName()) ? invoice.getEmployeeName() : "Valued Customer") + ",</p>" +
               "<p>Please find attached invoice <strong>#" + invoice.getInvoiceNumber() + "</strong> from <strong>" + senderName + "</strong>.</p>" +
               "<p>If you have any questions, please feel free to reply directly to this email.</p>" +
               "<br><p>Best regards,<br><strong>" + senderName + " Team</strong></p>" +
               "</div></body></html>";
    }
}
