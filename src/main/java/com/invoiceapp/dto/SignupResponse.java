package com.invoiceapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignupResponse {
    private String token; // JWT token
    private String userId;
    private String email;
    private String name;
    private CompanyInfoDTO companyInfo;

    public SignupResponse() {
    }

    public SignupResponse(String token, String userId, String email, String name) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.companyInfo = null;
    }

    public SignupResponse(String token, String userId, String email, String name, CompanyInfoDTO companyInfo) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.companyInfo = companyInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompanyInfoDTO getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfoDTO companyInfo) {
        this.companyInfo = companyInfo;
    }
}
