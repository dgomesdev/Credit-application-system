# Credit-application-system

# Customer Controller

## Overview
The Customer Application API provides endpoints to manage customer information, including creating, retrieving, updating, and deleting customer records.

## Base URL
All endpoints are relative to the base URL: `/api/customers`

## Endpoints

### 1. Save Customer
- **Endpoint:** `POST /`
- **Description:** Creates a new customer record.
- **Request Body:**
  - Type: JSON
  - Content: [CustomerDto](#customerdto) object
- **Response:**
  - Status Code: 201 Created
  - Content: "Customer {firstName} saved!"


### 2. Find Customer by ID
- **Endpoint:** `GET /{id}`
- **Description:** Retrieves customer information by ID.
- **Path Parameters:**
  - `{id}`: ID of the customer to retrieve
- **Response:**
  - Status Code: 200 OK
  - Content: Detailed information about the customer
    ```
    Name: {firstName} {lastName}
    CPF: {cpf}
    Income: {income}
    E-mail: {email}
    Zip code: {zipCode}
    Street: {street}
    ```

### 3. Find All Customers
- **Endpoint:** `GET /`
- **Description:** Retrieves a list of all customers.
- **Response:**
  - Status Code: 200 OK
  - Content: List of [CustomerListView](#customerlistview) objects


### 4. Delete Customer
- **Endpoint:** `DELETE /{id}`
- **Description:** Deletes a customer record by ID.
- **Path Parameters:**
  - `{id}`: ID of the customer to delete
- **Response:**
  - Status Code: 204 No Content
  - Content: "Customer deleted successfully"


### 5. Update Customer
- **Endpoint:** `PATCH /`
- **Description:** Updates an existing customer record.
- **Request Parameters:**
  - `customerId`: ID of the customer to update
- **Request Body:**
  - Type: JSON
  - Content: [CustomerUpdateDto](#customerupdatedto) object
- **Response:**
  - Status Code: 200 OK
  - Content: "Customer updated successfully!"


## Data Transfer Objects (DTOs)

### CustomerDto
- **Attributes:**
  - `firstName` (String): First name of the customer
  - `lastName` (String): Last name of the customer
  - `cpf` (String): CPF (Brazilian Taxpayer Registry) of the customer
  - `income` (Double): Income of the customer
  - `email` (String): Email address of the customer
  - `zipCode` (String): ZIP code of the customer
  - `street` (String): Street address of the customer

### CustomerListView
- **Attributes:**
  - `id` (Long): ID of the customer
  - `firstName` (String): First name of the customer
  - `lastName` (String): Last name of the customer

### CustomerUpdateDto
- **Attributes:**
  - `firstName` (String): Updated first name of the customer
  - `lastName` (String): Updated last name of the customer
  - `income` (Double): Updated income of the customer
  - `email` (String): Updated email address of the customer
  - `zipCode` (String): Updated ZIP code of the customer
  - `street` (String): Updated street address of the customer

----------------------------------------------------------------------

 # Credit Controller

## Overview
The Credit Application API provides endpoints to manage credit information, including creating and retrieving credit records associated with customers.

## Base URL
All endpoints are relative to the base URL: `/api/credits`

## Endpoints

### 1. Save Credit
- **Endpoint:** `POST /`
- **Description:** Creates a new credit record for a customer.
- **Request Body:**
  - Type: JSON
  - Content: [CreditDto](#creditdto) object
- **Response:**
  - Status Code: 201 Created
  - Content: "Credit of customer with ID {customerId} saved successfully!"

### 2. Find All Credits By Customer ID
- **Endpoint:** `GET /`
- **Description:** Retrieves a list of all credit records for a specific customer.
- **Request Parameters:**
  - `customerId` (Long): ID of the customer
- **Response:**
  - Type: List of [CreditListView](#creditlistview) objects

### 3. Find Credit By Credit Code
- **Endpoint:** `GET /{creditCode}`
- **Description:** Retrieves detailed information about a specific credit record using its credit code.
- **Path Parameters:**
  - `{creditCode}` (UUID): Unique identifier for the credit record
- **Request Parameters:**
  - `customerId` (Long): ID of the customer associated with the credit record
- **Response:**
  - Status Code: 200 OK
  - Content: Detailed information about the credit record
    ```
    Credit code: {creditCode}
    Credit value: {creditValue}
    Number of installments: {numberOfInstallments}
    Credit status: {status}
    Customer's first name: {firstNameCustomer}
    Customer's last name: {lastNameCustomer}
    ```

## Data Transfer Objects (DTOs)

### CreditDto
- **Attributes:**
  - `customerId` (Long): ID of the customer associated with the credit
  - `creditValue` (Double): Value of the credit
  - `numberOfInstallments` (Int): Number of installments for the credit

### CreditListView
- **Attributes:**
  - `creditCode` (UUID): Unique identifier for the credit record
  - `creditValue` (Double): Value of the credit
  - `status` (String): Status of the credit

### CreditView
- **Attributes:**
  - `creditCode` (UUID): Unique identifier for the credit record
  - `creditValue` (Double): Value of the credit
  - `numberOfInstallments` (Int): Number of installments for the credit
  - `status` (String): Status of the credit
  - `firstNameCustomer` (String): First name of the customer associated with the credit
  - `lastNameCustomer` (String): Last name of the customer associated with the credit
