
package com.invoiceapp.service;

import com.invoiceapp.dto.*;
import com.invoiceapp.entity.BankDetails;
import com.invoiceapp.entity.CompanyInfo;
import com.invoiceapp.entity.User;
import com.invoiceapp.repository.CompanyInfoRepository;
import com.invoiceapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse login(LoginRequest loginRequest) {
        // Validate both email and password are provided
        if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        // Find user by email
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail().toLowerCase().trim());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        User user = userOpt.get();

        // Verify password using bcrypt
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Generate JWT token with only userId and email
        String token = jwtService.generateToken(user.getId(), user.getEmail());

        // Return only token, userId, and email (NOT full user object)
        return new LoginResponse(token, user.getId(), user.getEmail());
    }

    public SignupResponse signup(SignupRequest signupRequest) throws Exception {
        if (signupRequest == null) {
            throw new IllegalArgumentException("Signup request body is required");
        }

        // Validate email
        if (signupRequest.getEmail() == null || signupRequest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        String email = signupRequest.getEmail().trim().toLowerCase();
        if (email.length() > 254) {
            throw new IllegalArgumentException("Email cannot exceed 254 characters");
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Validate password
        if (signupRequest.getPassword() == null || signupRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (signupRequest.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        // Validate name / fullName
        String name = signupRequest.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        name = name.trim();
        if (name.length() > 100) {
            throw new IllegalArgumentException("Name cannot exceed 100 characters");
        }

        // Validate confirmPassword if provided
        if (signupRequest.getConfirmPassword() != null && !signupRequest.getConfirmPassword().trim().isEmpty()) {
            if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
                throw new IllegalArgumentException("Passwords do not match");
            }
        }

        // Check if user already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Create user
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setName(name);
        user.setCreatedAt(java.time.LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(savedUser.getId(), savedUser.getEmail());

        // Return SignupResponse with only account identity fields
        return new SignupResponse(token, savedUser.getId(), savedUser.getEmail(), savedUser.getName());
    }

    public CompanyInfoDTO updateCompanyInfo(String userId, CompanyUpdateRequest updateRequest) throws Exception {
        System.out.println("Updating company info for user: " + userId);
        Optional<CompanyInfo> companyOpt = companyInfoRepository.findByUserId(userId);
        CompanyInfo companyInfo;
        boolean isNew = false;

        if (companyOpt.isEmpty()) {
            companyInfo = new CompanyInfo();
            companyInfo.setUserId(userId);
            companyInfo.setCreatedAt(java.time.LocalDateTime.now());
            isNew = true;
        } else {
            companyInfo = companyOpt.get();
        }

        if (updateRequest.getCompanyName() != null)
            companyInfo.setCompanyName(updateRequest.getCompanyName().trim());
        if (updateRequest.getCompanyAddress() != null)
            companyInfo.setCompanyAddress(updateRequest.getCompanyAddress().trim());
        if (updateRequest.getInvoiceFormat() != null)
            companyInfo.setInvoiceFormat(updateRequest.getInvoiceFormat());
        if (updateRequest.getFromEmail() != null)
            companyInfo.setFromEmail(updateRequest.getFromEmail());

        if (updateRequest.getCompanyLogo() != null && !updateRequest.getCompanyLogo().isEmpty()) {
            companyInfo.setCompanyLogoUrl(convertToBase64(updateRequest.getCompanyLogo()));
        }

        if (updateRequest.getBankDetails() != null) {
            BankDetailsDTO bankDto = updateRequest.getBankDetails();
            BankDetails bankDetails = new BankDetails(
                    bankDto.getBankName(),
                    bankDto.getAccountNumber(),
                    bankDto.getAccountHolderName(),
                    bankDto.getIfscCode(),
                    bankDto.getSwiftCode(),
                    bankDto.getBankCode(),
                    bankDto.getBranchName(),
                    bankDto.getBranchCode(),
                    bankDto.getAccountType());
            companyInfo.setBankDetails(bankDetails);
        }

        companyInfo.setUpdatedAt(java.time.LocalDateTime.now());
        CompanyInfo savedCompany = companyInfoRepository.save(companyInfo);

        if (isNew) {
            userRepository.findById(userId).ifPresent(u -> {
                u.setCompanyInfoId(savedCompany.getId());
                userRepository.save(u);
            });
        }

        // Convert to DTO
        CompanyInfoDTO dto = new CompanyInfoDTO();
        dto.setId(savedCompany.getId());
        dto.setCompanyName(savedCompany.getCompanyName());
        dto.setCompanyAddress(savedCompany.getCompanyAddress());
        dto.setCompanyLogoUrl(savedCompany.getCompanyLogoUrl());
        dto.setInvoiceFormat(savedCompany.getInvoiceFormat());
        dto.setFromEmail(savedCompany.getFromEmail());

        if (savedCompany.getBankDetails() != null) {
            BankDetailsDTO bankDto = new BankDetailsDTO();
            bankDto.setBankName(savedCompany.getBankDetails().getBankName());
            bankDto.setAccountNumber(savedCompany.getBankDetails().getAccountNumber());
            bankDto.setAccountHolderName(savedCompany.getBankDetails().getAccountHolderName());
            bankDto.setIfscCode(savedCompany.getBankDetails().getIfscCode());
            bankDto.setBranchName(savedCompany.getBankDetails().getBranchName());
            bankDto.setBranchCode(savedCompany.getBankDetails().getBranchCode());
            bankDto.setAccountType(savedCompany.getBankDetails().getAccountType());
            bankDto.setSwiftCode(savedCompany.getBankDetails().getSwiftCode());
            dto.setBankDetails(bankDto);
        }

        return dto;
    }

    private String convertToBase64(org.springframework.web.multipart.MultipartFile file) throws java.io.IOException {
        byte[] bytes = file.getBytes();
        String base64 = java.util.Base64.getEncoder().encodeToString(bytes);
        return "data:" + file.getContentType() + ";base64," + base64;
    }

    // Helper method to hash password
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    // Get company info by userId
    public java.util.Optional<com.invoiceapp.entity.CompanyInfo> getCompanyInfoByUserId(String userId) {
        return companyInfoRepository.findByUserId(userId);
    }
}
