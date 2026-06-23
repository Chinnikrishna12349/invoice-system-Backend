package com.invoiceapp.service;

import com.invoiceapp.dto.InvoiceDTO;
import java.util.List;

public interface EmailService {
    public static class Attachment {
        private String name;
        private String content; // base64 encoded

        public Attachment() {}
        public Attachment(String name, String content) {
            this.name = name;
            this.content = content;
        }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    void sendInvoiceEmail(InvoiceDTO invoice);

    void sendInvoiceEmail(InvoiceDTO invoice, List<String> additionalEmails);

    void sendInvoiceEmail(InvoiceDTO invoice, String recipientEmail);

    void sendInvoiceEmailWithPdf(InvoiceDTO invoice, byte[] pdfBytes);

    void sendInvoiceEmailWithPdf(InvoiceDTO invoice, byte[] pdfBytes, List<String> additionalEmails);

    void sendInvoiceEmailWithMultiplePdfs(InvoiceDTO invoice, List<Attachment> attachments, List<String> additionalEmails);
}
