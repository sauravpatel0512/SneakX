# SneakX – Shoe Marketplace App
## UML Document – Increment I Delivery

| Field | Detail |
|---|---|
| **Project** | SneakX – Shoe Marketplace App |
| **Team No.** | 3 |
| **Class** | CSE 3310; Spring 2026 |
| **Module** | Unified Modeling Language (UML) |
| **Deliverable** | UML Document – Increment I Delivery |
| **Version** | 1.0 |
| **Date** | 02/10/2026 |

---

## Contributors

1. Saurav Patel (1002265166)
2. Jignesha Bendale
3. Shishir Sunar
4. Ishika Sanghadia

---

## Revision History

| Version | Date | Originator | Reason for Change | Description of Changes |
|---|---|---|---|---|
| 1.0 | 02/10/2026 | Team 3 | Initial draft | Initial UML document draft |

---

## Table of Contents

1. [Introduction and Project Overview](#1-introduction-and-project-overview)
2. [Project Context Diagram](#2-project-context-diagram)
3. [UML Diagrams](#3-uml-diagrams)
   - 3.1 [UML Diagram: "System"](#31-uml-diagram-system)
   - 3.2 [UML Diagram: "Registration and Login"](#32-uml-diagram-registration-and-login)
   - 3.3 [UML Diagram: "Payments"](#33-uml-diagram-payments)
   - 3.4 [UML Diagram: "Search"](#34-uml-diagram-search)

---

## 1. Introduction and Project Overview

The purpose of this project is to develop a mobile marketplace application called **SneakX** that allows users to buy and sell shoes through a secure and user-friendly Android platform. The application provides a streamlined environment where buyers can browse listings, search and filter products, add items to a cart, and complete purchases using a mock payment system. Sellers can create and manage shoe listings, while administrators oversee users and listings to ensure proper marketplace functionality.

SneakX consists of the following main components:

- **User Authentication:** Users can register, log in, and log out securely. The system supports different roles including Buyer, Seller (switchable), and Admin.
- **Buyer Module:** Buyers can browse shoe listings, search and filter products by keyword, category, and price range, view product details, add items to their cart, and complete checkout using a mock payment flow.
- **Seller Module:** Sellers can create, edit, and delete shoe listings, upload photos, manage product details, and monitor their active listings.
- **Admin Module:** Admin users can review users and listings, enable or disable accounts, and monitor overall system activity.
- **Cart and Checkout System:** Buyers can manage cart items, update quantities, and confirm purchases through a structured checkout process.
- **Search and Filtering System:** The platform provides search functionality and filtering options to help users efficiently locate relevant products.
- **Profile and Settings:** Users can edit personal information and customize application settings such as theme preferences.

The architecture of SneakX emphasizes modularity and clear separation of responsibilities between different system components. The UML diagrams included in this document model the structure and behavior of the system, including its overall class structure, registration and login workflow, payment process, and search functionality. These models provide a structured representation of SneakX's design and support future implementation and testing phases.

---

## 2. Project Context Diagram

The context diagram illustrates the high-level interactions between external actors and the SneakX Core System within the SneakX Mobile App boundary.

**Actors and Interactions:**

| Actor | Interaction | Direction |
|---|---|---|
| **Buyer** | 1. Search, View, Buy | → SneakX Core System |
| **Seller** | 2. List Shoes, Manage Orders | → SneakX Core System |
| **Admin** | 3. Moderate Users & Listings | → SneakX Core System |
| **Payment Gateway (Mock)** | 4. Process Payment | SneakX Core System → |
| **Payment Gateway (Mock)** | 5. Confirm Transaction | → SneakX Core System |

**SneakX Core System** (internal components):
- Authentication
- Inventory Management
- Order Processing
- Search Engine

---

## 3. UML Diagrams

### 3.1 UML Diagram: "System"

This is a **class diagram** showing the core domain model of SneakX.

#### Enumerations

**`«enumeration» Role`**
```
BUYER
SELLER
ADMIN
```

**`«enumeration» ListingStatus`**
```
ACTIVE
SOLD
DISABLED
```

**`«enumeration» OrderStatus`**
```
PENDING
PAID
CANCELLED
```

#### Classes

**`User`**
```
+ String id
+ String name
+ String email
+ Role role
+ String avatarUrl
```

**`Order`**
```
+ String id
+ String buyerId
+ double total
+ String paymentMethod
+ String shippingInfo
+ OrderStatus status
+ Date createdAt
```

**`CartItem`**
```
+ String listingId
+ int quantity
```

**`Listing`**
```
+ String id
+ String sellerId
+ String title
+ String description
+ String category
+ double price
+ String condition
+ String[] photos
+ ListingStatus status
+ Date createdAt
```

#### Relationships

| From | Relationship | To | Multiplicity |
|---|---|---|---|
| User | places (Buyer) | Order | 1 → 0..* |
| Order | creates (Seller) | CartItem | 1 → 1..* |
| CartItem | references | Listing | 0..* → 1 |

---

### 3.2 UML Diagram: "Registration and Login"

This is a **state machine diagram** modeling the authentication lifecycle.

#### States

| State | Description |
|---|---|
| `LoggedOut` | Initial state; user is not authenticated |
| `LoggingIn` | User has initiated login |
| `LoggedIn` | User successfully authenticated |
| `LoginFailed` | Login attempt failed due to invalid credentials |
| `Registering` | User is completing the registration form |

#### Transitions

| From State | Trigger / Guard | To State |
|---|---|---|
| *(Initial)* | — | `LoggedOut` |
| `LoggedOut` | Login | `LoggingIn` |
| `LoggedOut` | Register | `Registering` |
| `LoggingIn` | Valid Credentials | `LoggedIn` |
| `LoggingIn` | Invalid Credentials | `LoginFailed` |
| `LoggedIn` | Logout | `LoggedOut` |
| `LoginFailed` | Retry | `LoggingIn` |
| `LoginFailed` | Cancel | `LoggedOut` |
| `Registering` | Registration Success | `LoggedOut` |
| `Registering` | Validation Error | `Registering` (self-loop) |

---

### 3.3 UML Diagram: "Payments"

This is a **sequence diagram** showing the interaction between the Buyer, SneakX App, Order object, and the mock Payment Gateway during checkout.

#### Participants

- **Buyer** (actor)
- **SneakX App**
- **Order**
- **Payment Gateway (Mock)**

#### Message Flow

1. **Buyer → SneakX App:** `Tap Checkout`
2. **SneakX App → Buyer:** `Enter shipping info + choose payment method`
3. **Buyer → SneakX App:** `Place Order`
4. **SneakX App → Payment Gateway:** `ProcessPayment(total, method)`
5. **Payment Gateway → SneakX App:** `PaymentResult(success/fail)`

#### Alternative Fragment (`alt`)

**[Payment Success]**
6. **SneakX App → Order:** `Create Order + Items`
7. **Order → SneakX App:** `Order ID`
8. **SneakX App → Buyer:** `Show Order Confirmation`

**[Payment Failed]**
6. **SneakX App → Buyer:** `Show Payment Failed Message`

---

### 3.4 UML Diagram: "Search"

This is an **activity diagram** (flowchart) modeling the search and discovery flow for a user browsing listings.

#### Flow

```
[Start]
   ↓
Open Home Screen
   ↓
Enter keyword or select category
   ↓
Apply filters: category, price, sort
   ↓
Tap Search
   ↓
<Results Found?>
  ├── Yes → Display results list
  │            ↓
  │         Select listing
  │            ↓
  │         View listing details
  │            ↓
  │          [End]
  │
  └── No  → Show "No Results" message
               ↓
          (loop back to: Enter keyword or select category)
```
