# SneakX – Shoe Marketplace App: Test Plan

**Project:** SneakX – Shoe Marketplace App  
**Team No.:** 3  
**Class:** CSE 3310; Spring 2026  
**Module:** Test Plan  
**Deliverable:** Test Plan Document  
**Version:** 1.0  
**Date:** 04/11/2026  
**Prepared by:** Team 3

---

## Team Members

1. Saurav Patel (1002265166)
2. Jignesha Bendale (1002214866)
3. Ishika Sanghadia (1002133686)
4. Shishir Sunar (1002232718)

---

## Revision History

| Version | Date | Originator | Reason for Change | High Level Description |
|---------|------|------------|-------------------|------------------------|
| 1.0 | 04/11/2026 | Team 3 | Initial draft | Initial Test Plan document |

---

## Table of Contents

1. [Introduction and Plan of Approach](#1-introduction-and-plan-of-approach)
2. [System Overview](#2-system-overview)
3. [Systems Requirements](#3-systems-requirements)
4. [Test Cases](#4-test-cases)
5. [Software Processes and Infrastructure](#5-software-processes-and-infrastructure)
6. [Assumptions and Constraints](#6-assumptions-and-constraints)
7. [Delivery and Schedule](#7-delivery-and-schedule)
8. [Stakeholder Approval Form](#8-stakeholder-approval-form)
9. [Appendix](#9-appendix)

---

## 1. Introduction and Plan of Approach

This test plan defines the functional testing strategy for SneakX, a mobile marketplace application designed for buying and selling shoes. The system supports multiple user roles, including Buyer, Seller, and Admin, and provides features such as registration, login, product listing management, search and filtering, cart operations, checkout and payment, and administrative moderation.

Testing will be conducted in a structured, module-by-module manner. The testing process begins with core authentication features such as registration and login to ensure that users can securely access the system. Once authentication is validated, role-based access and navigation will be tested to confirm that Buyers, Sellers, and Admins have appropriate permissions.

After authentication, buyer-related features such as search, filtering, cart management, and checkout will be tested, followed by seller-specific listing management features. Administrative functionalities such as user and listing moderation will be validated afterward. Profile management will also be tested to ensure users can update their personal information and preferences.

Each module includes both valid (happy path) and invalid (error path) scenarios to ensure the system behaves correctly under all conditions. Expected results clearly define the correct system behavior, ensuring consistency and completeness in testing.

---

## 2. System Overview

### 2.1 Project Context Diagram

The SneakX system boundary encompasses the SneakX Core System, which includes Authentication, Inventory Management, Order Processing, and a Search Engine. Three actors interact with the system:

- **Buyer** → Search, View, Buy
- **Seller** → List Shoes, Manage Orders
- **Admin** → Moderate Users & Listings

The system also interfaces with an external **Payment Gateway (Mock Payment)** to process and confirm transactions.

### 2.2 UML Diagram: "System"

Key entities and enumerations in the system:

**Enumerations:**
- `Role`: BUYER, SELLER, ADMIN
- `ListingStatus`: ACTIVE, SOLD, DISABLED
- `OrderStatus`: PENDING, PAID, CANCELLED

**Classes:**
- `User`: id, name, email, role, avatarUrl
- `Cart`: id, buyerId, subtotal — has active association with User
- `Order`: id, buyerId, total, paymentMethod, shippingInfo, status (OrderStatus), createdAt — placed by Buyer
- `CartItem`: listingId, quantity — contained in Cart and Order (snapshot)
- `Listing`: id, sellerId, title, description, category, price, condition, photos[], status (ListingStatus), createdAt — created by Seller; referenced by CartItem

### 2.3 UML Diagram: "Registration and Login"

State flow:

```
[LoggedOut]
  ├── Login → [LoggingIn]
  │     ├── Valid Credentials → [LoggedIn] → Logout → [LoggedOut]
  │     └── Invalid Credentials → [LoginFailed] → Retry → [LoggingIn]
  ├── Cancel → [LoggedOut]
  └── Register → [Registering]
        ├── Validation Error → [Registering] (loop)
        └── Registration Success → [LoggedOut]
```

### 2.4 UML Diagram: "Payments"

Sequence flow between Buyer, SneakX App, Order, and Payment Gateway (Mock):

1. Buyer taps Checkout
2. App prompts for shipping info + payment method
3. Buyer places order → App calls `ProcessPayment(total, method)` on Payment Gateway
4. Gateway returns `PaymentResult(success/fail)`
   - **[Payment Success]:** Create Order + Items → return Order ID → Show Order Confirmation
   - **[Payment Failed]:** Show Payment Failed Message

### 2.5 UML Diagram: "Search"

Flowchart:

```
Start → Open Home Screen → Enter keyword or select category
      → Apply filters: category, price, sort → Tap Search
      → Results Found?
          ├── Yes → Display results list → Select listing → View listing details → End
          └── No  → Show "No Results" message → (return to search)
```

---

## 3. Systems Requirements

### 3.1 "Login" Requirements

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | User Authentication - Login |
| **Sequence No.** | SR-LOGIN-001 |
| **Short Description** | The system shall authenticate registered users before granting access to protected features. |
| **Description** | The system shall validate username and password credentials against stored user records and establish a session upon successful authentication. |
| **Pre-Conditions** | User account exists in the system database. User account is not disabled by an administrator. |
| **Post Conditions** | Successful login: User is redirected to role-based dashboard. Failed login: Error message displayed. After 5 consecutive failed attempts, account is temporarily locked. |
| **Priority** | High |
| **Risk** | Medium |
| **Testable** | Yes (response time ≤ 2 seconds) |

### 3.2 "Registration" Requirements

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | User Account Registration |
| **Sequence No.** | SR-REG-001 |
| **Short Description** | The system shall allow new users to create an account with required personal information. |
| **Description** | The system shall collect required fields (name, email, password, role selection) and create a unique user account after validating input data. |
| **Pre-Conditions** | User does not already have a registered account with the same email. |
| **Post Conditions** | Account is created and stored. User is redirected to login screen. If validation fails, error message is displayed. |
| **Priority** | High |
| **Validation** | All required fields must be completed |
| **Testable** | Account creation completes within 3 seconds |

### 3.3 "Role Management" Requirements

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Role-Based Access Control |
| **Sequence No.** | SR-ROLE-001 |
| **Short Description** | The system shall restrict system features based on assigned user role. |
| **Description** | The system shall grant access permissions according to user role (Buyer, Seller, Admin) and prevent unauthorized feature access. |
| **Pre-Conditions** | User is authenticated. |
| **Post Conditions** | User dashboard displays role-specific features. Unauthorized actions are blocked. |
| **Security** | Mandatory |
| **Testable** | Unauthorized role access attempts are denied 100% of the time |

### 3.4 "Product Listing" Requirements

#### SR-LIST-001: Create Product Listing

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Create Product Listing |
| **Sequence No.** | SR-LIST-001 |
| **Short Description** | The system shall allow sellers to create new shoe listings. |
| **Description** | The system shall enable sellers to input product name, description, category, price, and image, and publish the listing after validation. |
| **Pre-Conditions** | Seller is authenticated. Seller account is active. |
| **Post Conditions** | Listing appears in marketplace. Listing stored in system database. |
| **Validation** | All required fields must be completed |
| **Testable** | Listing published within 3 seconds |

#### SR-LIST-002: Edit Product Listing

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Edit Product Listing |
| **Sequence No.** | SR-LIST-002 |
| **Short Description** | The system shall allow sellers to modify existing listings. |
| **Description** | The system shall permit sellers to update listing information and save changes. |
| **Pre-Conditions** | Seller owns the listing. Seller is authenticated. |
| **Post Conditions** | Updated information reflected immediately. Old data replaced. |
| **Audit** | Changes must be timestamped |
| **Testable** | Update reflected within 2 seconds |

#### SR-LIST-003: Delete Product Listing

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Delete Product Listing |
| **Sequence No.** | SR-LIST-003 |
| **Short Description** | The system shall allow sellers to remove active listings. |
| **Description** | The system shall remove the selected listing from marketplace visibility. |
| **Pre-Conditions** | Seller owns listing. Seller authenticated. |
| **Post Conditions** | Listing no longer visible to buyers. |
| **Testable** | Listing removal occurs immediately |

### 3.5 "Search" Requirements

#### SR-SEARCH-001: Keyword Search

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Keyword Search |
| **Sequence No.** | SR-SEARCH-001 |
| **Short Description** | The system shall allow users to search listings using keywords. |
| **Description** | The system shall retrieve listings matching entered keywords in title or description. |
| **Pre-Conditions** | User is authenticated. |
| **Post Conditions** | Search results displayed. If no match found, system displays "No Results" message. |
| **Performance** | Results returned within 3 seconds |
| **Accuracy** | Results must match keyword criteria |

#### SR-SEARCH-002: Filter by Category and Price

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Filter by Category and Price |
| **Sequence No.** | SR-SEARCH-002 |
| **Short Description** | The system shall allow users to filter listings by category and price range. |
| **Description** | The system shall narrow search results based on selected category and user-defined price range. |
| **Pre-Conditions** | Listings exist in system. |
| **Post Conditions** | Filtered results displayed. |
| **Performance** | Results within 3 seconds |
| **Testable** | Filtering logic returns only matching entries |

### 3.6 "Cart" Requirements

#### SR-CART-001: Add Item to Cart

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Add Item to Cart |
| **Sequence No.** | SR-CART-001 |
| **Short Description** | The system shall allow buyers to add products to their shopping cart. |
| **Description** | The system shall store selected items in the buyer's cart until checkout or removal. |
| **Pre-Conditions** | Buyer authenticated. Product available. |
| **Post Conditions** | Item appears in cart. Cart total updated. |
| **Testable** | Cart updates instantly |

#### SR-CART-002: Update Cart Quantity

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Update Cart Quantity |
| **Sequence No.** | SR-CART-002 |
| **Short Description** | The system shall allow buyers to modify item quantities in the cart. |
| **Description** | The system shall recalculate total cost when quantity changes. |
| **Pre-Conditions** | Item exists in cart. |
| **Post Conditions** | Updated total displayed. |
| **Accuracy** | Total calculation must be correct to 2 decimal places |

#### SR-CART-003: Remove Item from Cart

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Remove Item from Cart |
| **Sequence No.** | SR-CART-003 |
| **Short Description** | The system shall allow buyers to remove items from cart. |
| **Description** | The system shall delete selected item and update cart total. |
| **Pre-Conditions** | Item exists in cart. |
| **Post Conditions** | Item removed. Cart total recalculated. |
| **Testable** | Removal reflected immediately |

### 3.7 "Checkout" Requirements

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Checkout Confirmation |
| **Sequence No.** | SR-CHECK-001 |
| **Short Description** | The system shall allow buyers to confirm purchase through mock payment process. |
| **Description** | The system shall simulate payment validation and generate order confirmation. |
| **Pre-Conditions** | Cart contains at least one item. Buyer authenticated. |
| **Post Conditions** | Order confirmation displayed. Cart cleared. |
| **Performance** | Confirmation generated within 3 seconds |
| **Note** | Payment is simulated only |

### 3.8 "Admin" Requirements

#### SR-ADMIN-001: Disable User Account

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Disable User Account |
| **Sequence No.** | SR-ADMIN-001 |
| **Short Description** | The system shall allow administrators to disable user accounts. |
| **Description** | Admin can deactivate user accounts, preventing login and system access. |
| **Pre-Conditions** | Admin authenticated. |
| **Post Conditions** | User account status changed to inactive. Disabled user cannot log in. |
| **Security** | Immediate enforcement required |

#### SR-ADMIN-002: Remove Inappropriate Listing

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Remove Inappropriate Listing |
| **Sequence No.** | SR-ADMIN-002 |
| **Short Description** | The system shall allow administrators to remove listings. |
| **Description** | Admin may delete listings violating marketplace policies. |
| **Pre-Conditions** | Listing exists. |
| **Post Conditions** | Listing removed from marketplace. |
| **Audit** | Admin action logged with timestamp |

### 3.9 "Profile" Requirements

| Attribute | Detail |
|-----------|--------|
| **Requirement Title** | Update User Profile |
| **Sequence No.** | SR-PROFILE-001 |
| **Short Description** | The system shall allow users to update profile information. |
| **Description** | Users may edit personal information such as name, email, or preferences. |
| **Pre-Conditions** | User authenticated. |
| **Post Conditions** | Profile changes saved. |
| **Validation** | Email must remain unique |

---

## 4. Test Cases

### 4.1 Registration

**Test Case ID:** `CSE3310/Spring 2026/Team3/Registration`

| TC No. | Test Case Description | Expected Results | Outcome |
|--------|-----------------------|------------------|---------|
| TC1A | Enter a new name, unique email, valid password, and select Buyer role, then submit registration. | A new user account is created and stored. The user is redirected to the login screen. A success message confirming registration is displayed. | |
| TC1B | Enter a new name, unique email, valid password, and select Seller role, then submit registration. | A new seller account is created successfully and saved. The user is redirected to the login screen. | |
| TC2 | Leave one or more required fields blank and attempt to register. | Registration is not completed. A validation error message is displayed indicating that all required fields must be completed. The form remains visible. | |
| TC3 | Enter an email address that already exists in the system and attempt to register. | Registration is rejected. A message is displayed stating that the email is already registered and the user must use a different email. | |
| TC4 | Enter invalid input such as an improperly formatted email or invalid password format and submit the form. | Registration is not completed. A validation error message is shown and the user remains on the registration screen. | |

### 4.2 Login

**Test Case ID:** `CSE3310/Spring 2026/Team3/Login`

| TC No. | Test Case Description | Expected Results | Outcome |
|--------|-----------------------|------------------|---------|
| TC1A | Enter valid Buyer credentials and submit login. | The user is authenticated successfully and redirected to the Buyer dashboard. Protected features become accessible. | |
| TC1B | Enter valid Seller credentials and submit login. | The user is authenticated successfully and redirected to the Seller dashboard. Seller functions become available. | |
| TC1C | Enter valid Admin credentials and submit login. | The user is authenticated successfully and redirected to the Admin dashboard with admin controls visible. | |
| TC2 | Enter a valid email but incorrect password and attempt to log in. | Login fails. An error message is displayed indicating invalid credentials. The user remains on the login page. | |
| TC3 | Enter an email that does not exist in the system and attempt to log in. | Login fails. An invalid credentials or account not found message is displayed. No dashboard is opened. | |
| TC4 | Attempt to log in using a disabled user account. | Login is blocked. A message is displayed indicating that the account is disabled or inactive, and protected features remain inaccessible. | |
| TC5 | Enter incorrect credentials five consecutive times. | After the fifth failed attempt, the account is temporarily locked and further login attempts are blocked until lockout conditions are cleared. | |
| TC6 | Log in successfully and then choose Logout. | The user session is ended and the system returns to the logged-out state or login screen. Protected pages are no longer accessible without logging in again. | |

### 4.3 Role Management

**Test Case ID:** `CSE3310/Spring 2026/Team3/RoleManagement`

| TC No. | Test Case Description | Expected Results | Outcome |
|--------|-----------------------|------------------|---------|
| TC1 | Log in as a Buyer and attempt to access Seller-only features such as creating a listing. | Access is denied. Seller-only controls are hidden or blocked, and the Buyer cannot create a listing. | |
| TC2 | Log in as a Seller and verify access to listing creation, editing, and deletion features. | Seller-specific features are visible and usable. The seller can manage only their own listings. | |
| TC3 | Log in as an Admin and access admin moderation features. | Admin-specific controls such as disabling users and removing listings are available. | |
| TC4 | Attempt to access an Admin-only function while logged in as Buyer or Seller. | The action is denied and the unauthorized function does not execute. | |

### 4.4 Product Listing Management

**Test Case ID:** `CSE3310/Spring 2026/Team3/ProductListingManagement`

| TC No. | Test Case Description | Expected Results | Outcome |
|--------|-----------------------|------------------|---------|
| TC1A | Log in as Seller, enter all required listing fields including title, description, category, price, condition, and image, then submit. | The listing is created successfully, stored in the system, and appears in the marketplace with status Active. | |
| TC1B | Attempt to create a listing while leaving one or more required fields blank. | Listing creation is rejected. A validation message is displayed and the incomplete listing is not published. | |
| TC2 | Edit an existing listing owned by the logged-in seller and save the changes. | The updated listing information appears immediately and the old information is replaced. | |
| TC3 | Attempt to edit a listing that is not owned by the logged-in seller. | The edit action is blocked and no changes are saved to the listing. | |
| TC4 | Delete a listing owned by the logged-in seller. | The listing is removed from marketplace visibility and no longer appears in active seller listings. | |
| TC5 | Attempt to create, edit, or delete a listing while logged in as Buyer. | The action is blocked because Buyers are not authorized to manage seller listings. | |

### 4.5 Search and Filter

**Test Case ID:** `CSE3310/Spring 2026/Team3/SearchAndFilter`

| TC No. | Test Case Description | Expected Results | Outcome |
|--------|-----------------------|------------------|---------|
| TC1 | Open the home screen, enter a keyword that matches an existing listing title or description, and tap Search. | A results list is displayed containing only listings matching the entered keyword. | |
| TC2 | Enter a keyword that does not match any listing and tap Search. | The system displays a "No Results" message and no listings are shown in the result list. | |
| TC3 | Apply a category filter and search. | Only listings in the selected category are shown. Listings outside that category are excluded. | |
| TC4 | Apply a price range filter and search. | Only listings whose prices fall within the selected price range are displayed. | |
| TC5 | Apply both category and price filters together and perform a search. | Only listings matching all applied filter conditions are displayed. | |
| TC6 | Select a listing from the displayed results. | The system opens the listing details screen and shows the complete product information. | |

### 4.6 Cart Management

**Test Case ID:** `CSE3310/Spring 2026/Team3/CartManagement`

| TC No. | Test Case Description | Expected Results | Outcome |
|--------|-----------------------|------------------|---------|
| TC1 | Log in as Buyer, open a product detail page, and add an available item to the cart. | The item is added to the cart successfully and the cart subtotal is updated immediately. | |
| TC2 | Add multiple items to the cart. | All selected items appear in the cart and the subtotal reflects the combined total of the items. | |
| TC3 | Change the quantity of an item already in the cart. | The quantity is updated and the total cost is recalculated correctly to two decimal places. | |
| TC4 | Remove an item from the cart. | The selected item is removed and the cart total is recalculated immediately. | |
| TC5 | Attempt to proceed with an empty cart. | Checkout is not allowed and the system indicates that at least one item is required in the cart before continuing. | |

### 4.7 Checkout and Payment

**Test Case ID:** `CSE3310/Spring 2026/Team3/CheckoutAndPayment`

| TC No. | Test Case Description | Expected Results | Outcome |
|--------|-----------------------|------------------|---------|
| TC1A | With at least one item in the cart, tap Checkout, enter shipping information, choose a payment method, and place the order using a successful mock payment result. | The payment is processed successfully, an order is created, an order ID is generated, an order confirmation screen is displayed, and the cart is cleared. | |
| TC1B | With items in the cart, tap Checkout, enter shipping information, choose a payment method, and place the order using a failed mock payment result. | The system displays a payment failed message. No completed order confirmation is shown, and the user is informed that payment was unsuccessful. | |
| TC2 | Attempt checkout with missing shipping information. | The order is not placed. A validation error message is displayed and the checkout screen remains open for correction. | |
| TC3 | Attempt checkout without selecting a payment method. | The order is not processed and the user is prompted to select a payment method before continuing. | |
| TC4 | Complete a successful checkout and verify order creation. | The system stores the order with buyer ID, total amount, payment method, shipping information, order status, and created date. | |

### 4.8 Admin Management

**Test Case ID:** `CSE3310/Spring 2026/Team3/AdminManagement`

| TC No. | Test Case Description | Expected Results | Outcome |
|--------|-----------------------|------------------|---------|
| TC1 | Log in as Admin and disable an active user account. | The selected user account status is changed to inactive or disabled immediately. The user can no longer log in. | |
| TC2 | Attempt to log in using a user account that was disabled by Admin. | Login is denied and the system displays a message indicating that the account is disabled. | |
| TC3 | Log in as Admin and remove an inappropriate listing. | The selected listing is removed from the marketplace and is no longer visible to buyers. | |
| TC4 | Verify that a non-admin user cannot access admin moderation features. | Buyer and Seller users are blocked from performing admin-only actions. | |

### 4.9 Profile Management

**Test Case ID:** `CSE3310/Spring 2026/Team3/ProfileManagement`

| TC No. | Test Case Description | Expected Results | Outcome |
|--------|-----------------------|------------------|---------|
| TC1 | Log in and update profile information such as name or preferences, then save changes. | The updated profile information is saved successfully and is displayed when the profile is reopened. | |
| TC2 | Change the email address to a new unique email and save. | The new email is accepted and stored successfully. Future login uses the updated email. | |
| TC3 | Attempt to change the email address to one already used by another account. | The profile update is rejected and a validation message indicates that the email must be unique. | |
| TC4 | Update profile settings such as theme or user preferences. | The selected preference is saved and remains applied when the user revisits settings. | |

---

## 5. Software Processes and Infrastructure

SneakX will follow an iterative development model with structured increments:

- Requirements definition
- UML modeling
- Incremental feature implementation
- Unit testing
- Integration testing
- Version control using Git

Requirement updates will be tracked using version numbers and documented in revision history.

### 5.1 Hardware and Infrastructure

| Component | Details |
|-----------|---------|
| Android Version | Android 12 or higher |
| Development Environment | Android Studio (latest stable version) |
| Programming Language | Java/Kotlin |
| Version Control | GitHub |
| Testing | Android Emulator and physical Android device |
| Database | Local persistent storage (Room/SQLite simulation) |

---

## 6. Assumptions and Constraints

### 6.1 Assumptions

- Users have stable internet connectivity.
- Android devices meet minimum OS requirements.
- Payment system is simulated and not connected to real financial institutions.
- Users provide accurate listing information.

### 6.2 Constraints

- Platform limited to Android devices only.
- No real-world payment processing integration.
- Development timeframe limited to semester duration.
- No third-party API integration allowed.

### 6.3 Out of Scope Material

- Real payment gateway integration.
- Shipping logistics integration.
- Advanced AI-based product recommendations.
- Multi-language support.
- Web-based version of the application.

---

## 7. Delivery and Schedule

| Task / Milestone | Anticipated Start | Anticipated End | Status | Comments |
|------------------|-------------------|-----------------|--------|----------|
| Prepare Requirements and UML diagram | 02/08/2026 | 02/13/2026 | Completed | Initial system modeling |
| SRA document (includes project objectives, requirements, and UML diagrams) | 02/26/2026 | 03/02/2026 | Completed | System requirements finalized |
| Login and registration module | 03/10/2026 | 03/18/2026 | Pending | Authentication functionality |
| Product listing and search module | 03/18/2026 | 04/02/2026 | Pending | Seller listing and buyer search |
| Admin management features | 04/02/2026 | 04/15/2026 | Pending | Account and listing moderation |
| System design implementation | 03/05/2026 | 03/25/2026 | Pending | Begin development of core features |
| Test case design | 03/25/2026 | 04/05/2026 | Pending | Creating and validating test cases |
| External documentation (e.g., User Manual) | 04/05/2026 | 04/12/2026 | Pending | Documentation for users |
| Project presentation | 04/15/2026 | 04/22/2026 | Pending | In-class presentation |
| Final Milestone: project delivery | 04/24/2026 | 04/24/2026 | Pending | Final system submission |

---

## 8. Stakeholder Approval Form

| Stakeholder Name | Stakeholder Role | Stakeholder Comments | Approval Signature and Date |
|------------------|------------------|----------------------|-----------------------------|
| Saurav Patel | Project Lead | Requirements match system scope. | |
| Jignesha Bendale | Developer | Architecture supports local storage. | |
| Shishir Sunar | Developer | Role-based permissions validated. | |
| Ishika Sanghadia | QA / Testing | All requirements are testable. | |

---

## 9. Appendix

### Appendix A – Glossary of Terms

| Term | Definition |
|------|------------|
| **Buyer** | A registered user who purchases products from the marketplace. |
| **Seller** | A registered user who creates and manages product listings. |
| **Administrator** | A privileged user responsible for moderating accounts and listings. |
| **RBAC** | Role-Based Access Control — a security model where system permissions are assigned based on user roles. |
| **Mock Payment** | A simulated payment process used for testing without real financial transactions. |
| **Listing** | A product entry created by a seller containing name, description, category, price, and image. |
| **Session** | A temporary authenticated interaction between the user and the system. |
| **Authentication** | The process of validating user credentials before granting access. |
| **Authorization** | The process of granting system access based on user role. |

### Appendix B – Requirement Traceability Matrix

| Requirement ID | Business Objective (BO) | System Objective (SO) | Test Case ID |
|----------------|-------------------------|-----------------------|--------------|
| SR-LOGIN-001 | BO-4 | SO-1 | TC-LOGIN-01 |
| SR-REG-001 | BO-3 | SO-4 | TC-REG-01 |
| SR-ROLE-001 | — | SO-3 | TC-ROLE-01 |
| SR-LIST-001 | BO-3 | SO-4 | TC-LIST-01 |
| SR-LIST-002 | BO-3 | SO-4 | TC-LIST-02 |
| SR-LIST-003 | BO-3 | SO-4 | TC-LIST-03 |
| SR-SEARCH-001 | BO-1 | SO-6 | TC-SEARCH-01 |
| SR-SEARCH-002 | BO-1 | SO-6 | TC-SEARCH-02 |
| SR-CART-001 | BO-1 | SO-5 | TC-CART-01 |
| SR-CART-002 | BO-1 | SO-5 | TC-CART-02 |
| SR-CART-003 | BO-1 | SO-5 | TC-CART-03 |
| SR-CHECK-001 | BO-1 | SO-2 | TC-CHECK-01 |
| SR-ADMIN-001 | BO-2 | SO-7 | TC-ADMIN-01 |
| SR-ADMIN-002 | BO-2 | SO-7 | TC-ADMIN-02 |
| SR-PROFILE-001 | — | SO-5 | TC-PROFILE-01 |
