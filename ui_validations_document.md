# UI Validations Document

This document outlines the UI validation rules implemented across the frontend forms in the invoice system project. These can be used by the QA team to test validation messages, mandatory fields, constraints, and constraints on user input.

## 1. Sign Up Form (`SignupForm.tsx`)

| Field Name | Mandatory / Optional | Validation Rules | Error Message (if invalid) |
| :--- | :--- | :--- | :--- |
| **Full Name** | Mandatory | Must not be empty or only spaces. | `Name is required` |
| **Email** | Mandatory | Must follow standard email format (`user@domain.com`). | `Email is required` / `Invalid email format` |
| **Password** | Mandatory | Minimum 6 characters. | `Password is required` / `Password must be at least 6 characters` |
| **Confirm Password** | Mandatory | Minimum 6 characters. Must exactly match the Password field. | `Passwords do not match` |

## 2. Login Form (`LoginForm.tsx`)

| Field Name | Mandatory / Optional | Validation Rules | Error Message (if invalid) |
| :--- | :--- | :--- | :--- |
| **Email** | Mandatory | Must be filled in (relies on HTML5 `required`). | Standard browser validation popup |
| **Password** | Mandatory | Must be filled in (relies on HTML5 `required`). | Standard browser validation popup |

## 3. Invoice Form (`InvoiceForm.tsx`)

This form contains several sections. Below are the validations for each.

### 3.1 General Invoice Details
| Field Name | Mandatory / Optional | Validation Rules | Error Message (if invalid) |
| :--- | :--- | :--- | :--- |
| **Invoice Date** | Mandatory | Must have a valid date selected. | `Invoice Date is required` |
| **Due Date** | Mandatory | Must have a valid date selected. | `Due Date is required` |
| **Sender Company** | Mandatory | Must be selected. If entered manually, must pass company name validation. | `Please select a sender company` / `Sender Company is required` / (Company name validation error) |
| **Sender Email** | Optional | If provided, must be a valid email format. | (Email validation error) |
| **PO Number** | Optional | No explicit length limits defined. | N/A |

### 3.2 Client / Billed To Details
| Field Name | Mandatory / Optional | Validation Rules | Error Message (if invalid) |
| :--- | :--- | :--- | :--- |
| **Client / Company Name** | Mandatory | Depends on client type. Must pass `validateCompanyName` or `validateEmployeeName`. | `Company Name is required` / `Employee Name is required` / (Name validation error) |
| **Client Email** | Optional | If provided, must be a valid email format. | (Email validation error) |
| **Client Address** | Mandatory | Must not be empty. | `Address is required` |
| **Client Phone** | Mandatory (for Company) / Optional (for Individual) | Required if the client type is 'company'. | `Phone is required` |

### 3.3 Services / Line Items
| Field Name | Mandatory / Optional | Validation Rules | Error Message (if invalid) |
| :--- | :--- | :--- | :--- |
| **Services List** | Mandatory | There must be at least 1 service item. | `At least one service is required` |
| **Description** | Mandatory | Description for each service row cannot be empty. | `Description required` |
| **Hours / Quantity**| Mandatory | Must be greater than 0. | `Hours > 0` |
| **Rate** | Mandatory | Must be greater than 0. | `Rate > 0` |

### 3.4 Bank Details (`BankDetailsForm.tsx`)
| Field Name | Mandatory / Optional | Validation Rules | Error Message (if invalid) |
| :--- | :--- | :--- | :--- |
| **Bank Name** | Mandatory | Must not be empty. | `Bank name is required` |
| **Account Number** | Mandatory | Numeric only. Max 18 digits (7 digits for Japan invoices). | `Account number is required` |
| **Account Holder Name**| Mandatory | Max 50 characters. Allows English letters, spaces, and Japanese characters (Hiragana, Katakana, Kanji). | `Account holder name is required` |
| **IFSC Code** | Mandatory (for India) | Max 11 characters. Alphanumeric, automatically forced to uppercase. | `IFSC code is required` |
| **SWIFT Code** | Mandatory (for Int'l/Japan) | Max 11 characters. Alphanumeric, automatically forced to uppercase. | `Swift code is required for International invoices` |
| **Branch Name** | Mandatory | Must not be empty. | `Branch name is required` |
| **Bank Code** | Mandatory (for Int'l/Japan) | Numeric only. Max 4 digits for Japan. | `Bank code is required` / `Bank code must be 4 digits for Japan` |
| **Branch Code** | Mandatory (for Int'l/Japan) | Numeric only. Max 3 digits for Japan. | `Branch code is required` / `Branch code must be 3 digits for Japan` |
| **Account Type** | Mandatory | Must select either "Savings" or "Current". | `Account type is required` |

---
**Note for Testing:** 
- In many of the forms, trying to submit with invalid data will highlight the respective fields with a red border and display the error message in red text below the input field (with a subtle pulse animation for visibility).
- Input masking is applied on Bank Detail fields (e.g., `accountNumber` forces numeric inputs, instantly stripping out letters/symbols as the user types).
