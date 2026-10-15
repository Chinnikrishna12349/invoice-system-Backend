package com.invoiceapp.dto;

import org.springframework.web.multipart.MultipartFile;

public class CompanyUpdateRequest {
    private String companyName;
    private String companyAddress;
    private String invoiceFormat;
    private String fromEmail;
    private MultipartFile companyLogo;
    private BankDetailsDTO bankDetails;

    public CompanyUpdateRequest() {}

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getInvoiceFormat() {
        return invoiceFormat;
    }

    public void setInvoiceFormat(String invoiceFormat) {
        this.invoiceFormat = invoiceFormat;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public MultipartFile getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(MultipartFile companyLogo) {
        this.companyLogo = companyLogo;
    }

    public BankDetailsDTO getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetailsDTO bankDetails) {
        this.bankDetails = bankDetails;
    }
}
