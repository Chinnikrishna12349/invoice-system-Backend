package com.invoiceapp.dto;

import jakarta.validation.constraints.Size;

public class CompanyInfoDTO {
    private String id;
    private String companyName;
    @Size(max = 500, message = "Company address cannot exceed 500 characters")
    private String companyAddress;
    private String companyLogoUrl;
    private String invoiceFormat;
    private String fromEmail;
    private BankDetailsDTO bankDetails;

    public CompanyInfoDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public BankDetailsDTO getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetailsDTO bankDetails) {
        this.bankDetails = bankDetails;
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
}
