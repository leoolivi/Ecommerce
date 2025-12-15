# E-Commerce API Documentation

## Base URL
```
http://localhost:8080/api/v1
```

## Authentication

The API uses **JWT (JSON Web Token)** for authentication. Include the token in the `Authorization` header:

```
Authorization: Bearer <your_jwt_token>
```

### Default Test Users
```
Customer: mario.rossi@gmail.com / password
Customer: laura.bianchi@gmail.com / password
Admin: giuseppe.verdi@gmail.com / password
```

---

## Authentication Endpoints

### Register
```http
POST /auth/register
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:** `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "email": "user@example.com",
    "role": "CUSTOMER"
  }
}
```

**Notes:**
- New users are automatically assigned the `CUSTOMER` role
- Password is encrypted with BCrypt
- Token expires in 1 hour (3600 seconds)

### Login
```http
POST /auth/login
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:** `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "email": "user@example.com",
    "role": "CUSTOMER"
  }
}
```

**Errors:**
- `401 Unauthorized` - Invalid credentials
- `409 Conflict` - Email already exists (register only)

### Password Reset Request
```http
POST /auth/password-reset
```

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

**Response:** `200 OK`
```json
"Email sent to user@example.com with the code."
```

**Notes:**
- Generates a 4-digit OTP code
- Code expires after 10 minutes
- Email is sent via Mailtrap

### Password Reset Verification
```http
POST /auth/password-reset-check
```

**Request Body:**
```json
{
  "otp": "1234",
  "newPassword": "newpassword123"
}
```

**Response:** `200 OK`
```json
"Password changed successfully"
```

**Errors:**
- `404 Not Found` - OTP code not found
- `200 OK` with message "Code not valid" - OTP expired or invalid

---

## Products

### Get All Products
```http
GET /products
```
**Auth Required:** No (public endpoint)

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Wireless Bluetooth Headphones",
    "description": "Premium noise-cancelling headphones...",
    "price": 89.99,
    "stockQuantity": 45,
    "category": "Electronics",
    "imageUrl": "https://images.unsplash.com/photo-..."
  }
]
```

### Get Product by ID
```http
GET /products/{id}
```
**Auth Required:** No (public endpoint)

**Parameters:**
- `id` (path) - Product ID

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Wireless Bluetooth Headphones",
  "description": "Premium noise-cancelling headphones...",
  "price": 89.99,
  "stockQuantity": 45,
  "category": "Electronics",
  "imageUrl": "https://images.unsplash.com/photo-..."
}
```

**Errors:**
- `404 Not Found` - Product not found

### Search Products
```http
GET /products/search?q={query}
```
**Auth Required:** No (public endpoint)

**Query Parameters:**
- `q` (required) - Search query string (case-insensitive)

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Wireless Bluetooth Headphones",
    "description": "Premium noise-cancelling headphones...",
    "price": 89.99,
    "stockQuantity": 45,
    "category": "Electronics",
    "imageUrl": "https://images.unsplash.com/photo-..."
  }
]
```

---

## Orders (Customer)

### Get My Orders
```http
GET /orders
```
**Auth Required:** Yes (Customer or Admin)

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "products": [...],
    "subtotal": 109.98,
    "shippingAddress": "Via Roma 123, Roma",
    "customerId": 1,
    "payment": {
      "type": "CREDIT_CARD",
      "cardNumber": "1234567890123456",
      "cardHolder": "Mario Rossi",
      "expiryMonth": "12",
      "expiryYear": "2026",
      "cvc": "123"
    },
    "status": "PAID"
  }
]
```

**Notes:**
- Returns only orders belonging to the authenticated customer
- `customerId` is automatically set from JWT token

### Get Order by ID
```http
GET /orders/{id}
```
**Auth Required:** Yes

**Parameters:**
- `id` (path) - Order ID

**Response:** `200 OK`
```json
{
  "id": 1,
  "products": [...],
  "subtotal": 109.98,
  "shippingAddress": "Via Roma 123",
  "customerId": 1,
  "payment": {...},
  "status": "PAID"
}
```

**Errors:**
- `404 Not Found` - Order not found
- `401 Unauthorized` - Cannot read another customer's orders

### Create Order
```http
POST /orders
```
**Auth Required:** Yes

**Request Body:**
```json
{
  "productIds": [1, 2],
  "shippingAddress": "Via Roma 123, Roma, Italy",
  "paymentMethod": {
    "type": "CREDIT_CARD",
    "cardNumber": "1234567890123456",
    "cardHolder": "Mario Rossi",
    "expiryMonth": "12",
    "expiryYear": "2026",
    "cvc": "123"
  }
}
```

**Response:** `200 OK`
```json
"Order created successfully"
```

**Errors:**
- `400 Bad Request` - Payment processing failed
- `404 Not Found` - Setting not found (shipping_fee)

**Notes:**
- `customerId` is automatically extracted from JWT token
- Subtotal is automatically calculated including shipping fee
- Order status flow: CREATED → PAYING → PAID
- Card number must be at least 12 characters

### Update Order
```http
PUT /orders
```
**Auth Required:** Yes

**Request Body:**
```json
{
  "id": 1,
  "productIds": [1, 3],
  "shippingAddress": "Via Nuova 456",
  "orderStatus": "SHIPPED"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "products": [...],
  "subtotal": 119.98,
  "shippingAddress": "Via Nuova 456",
  "customerId": 1,
  "payment": {...},
  "status": "SHIPPED"
}
```

**Errors:**
- `404 Not Found` - Order not found
- `401 Unauthorized` - Cannot edit another customer's orders

### Cancel Order
```http
PATCH /orders/{id}
```
**Auth Required:** Yes

**Parameters:**
- `id` (path) - Order ID

**Response:** `200 OK`
```json
"Order canceled successfully"
```

**Errors:**
- `404 Not Found` - Order not found
- `401 Unauthorized` - Cannot cancel another customer's orders

---

## Admin Endpoints

All admin endpoints require the `ADMIN` role.

### Orders Management

#### Get All Orders (Admin)
```http
GET /admin/orders
```
**Auth Required:** Yes (Admin only)

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "products": [...],
    "subtotal": 109.98,
    "shippingAddress": "Via Roma 123",
    "customerId": 1,
    "payment": {...},
    "status": "PAID"
  }
]
```

#### Get Order by ID (Admin)
```http
GET /admin/orders/{id}
```
**Auth Required:** Yes (Admin only)

#### Create Order (Admin)
```http
POST /admin/orders
```
**Auth Required:** Yes (Admin only)

**Request Body:** Same as customer order creation

#### Update Order (Admin)
```http
PUT /admin/orders
```
**Auth Required:** Yes (Admin only)

**Request Body:** Same as customer order update

#### Delete Order (Admin)
```http
PUT /admin/orders/{id}
```
**Auth Required:** Yes (Admin only)

**Parameters:**
- `id` (path) - Order ID

**Response:** `200 OK`
```json
"Order deleted successfully"
```

### Products Management

#### Add Product
```http
POST /admin/products
```
**Auth Required:** Yes (Admin only)

**Request Body:**
```json
{
  "name": "New Product",
  "description": "Product description",
  "price": 29.99,
  "stockQuantity": 50,
  "category": "Electronics",
  "imageUrl": "https://example.com/image.jpg"
}
```

**Response:** `200 OK`
```json
"Product added successfully"
```

#### Update Product
```http
PUT /admin/products
```
**Auth Required:** Yes (Admin only)

**Request Body:**
```json
{
  "id": 1,
  "name": "Updated Product",
  "description": "Updated description",
  "price": 39.99,
  "stockQuantity": 30,
  "category": "Electronics",
  "imageUrl": "https://example.com/new-image.jpg"
}
```

**Response:** `200 OK`
```json
"Product updated successfully"
```

#### Delete Product
```http
DELETE /admin/products/{id}
```
**Auth Required:** Yes (Admin only)

**Parameters:**
- `id` (path) - Product ID

**Response:** `200 OK`
```json
"Product deleted successfully"
```

### User Management

#### Get All Users
```http
GET /admin/users
```
**Auth Required:** Yes (Admin only)

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "email": "user@example.com",
    "password": "$2a$12$...",
    "role": "CUSTOMER",
    "authorities": [...]
  }
]
```

#### Get User by ID
```http
GET /admin/users/{id}
```
**Auth Required:** Yes (Admin only)

**Parameters:**
- `id` (path) - User ID

#### Create User
```http
POST /admin/users
```
**Auth Required:** Yes (Admin only)

**Request Body:**
```json
{
  "email": "newuser@example.com",
  "password": "password123",
  "role": "CUSTOMER"
}
```

**Response:** `200 OK`
```json
{
  "id": 4,
  "email": "newuser@example.com",
  "role": "CUSTOMER"
}
```

**Errors:**
- `409 Conflict` - User already exists

#### Update User
```http
PUT /admin/users
```
**Auth Required:** Yes (Admin only)

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "newpassword",
  "role": "ADMIN"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "email": "user@example.com",
  "password": "$2a$12$...",
  "role": "ADMIN",
  "authorities": [...]
}
```

#### Delete User
```http
DELETE /admin/users/{id}
```
**Auth Required:** Yes (Admin only)

**Parameters:**
- `id` (path) - User ID

**Response:** `200 OK`

---

## Data Models

### User
```typescript
{
  id: number
  email: string           // unique, required
  password: string        // BCrypt hashed
  role: "CUSTOMER" | "ADMIN"
}
```

### Product
```typescript
{
  id: number
  name: string            // unique, required
  description: string     // optional
  price: number           // required
  stockQuantity: number   // required
  category: string        // required
  imageUrl: string        // required
}
```

### Order
```typescript
{
  id: number
  products: Product[]
  subtotal: number              // auto-calculated
  shippingAddress: string       // required
  customerId: number            // auto-set from JWT
  payment: PaymentMethod        // required
  status: OrderStatus           // default: CREATED
}
```

### PaymentMethod
```typescript
{
  type: "CREDIT_CARD" | "PAYPAL" | "STRIPE"
  cardNumber: string    // min 12 characters
  cardHolder: string
  expiryMonth: string
  expiryYear: string
  cvc: string
}
```

### OTPCode
```typescript
{
  id: number
  code: char[]              // 4-digit code
  issuedAt: LocalDateTime
  expiresAt: LocalDateTime  // +10 minutes from issuedAt
  userEmail: string
  userId: number
}
```

### AuthResponse
```typescript
{
  accessToken: string
  expiresIn: number     // seconds
  user: {
    id: number
    email: string
    role: "CUSTOMER" | "ADMIN"
  }
}
```

### OrderStatus Enum
- `CREATED` - Order has been created
- `PAYING` - Payment is being processed
- `PAID` - Payment successful
- `SHIPPED` - Order has been shipped
- `CANCELED` - Order has been canceled

### AppUserRole Enum
- `CUSTOMER` - Regular customer with basic permissions
- `ADMIN` - Administrator with full access

---

## Error Responses

All error responses follow this format:

```json
{
  "error": "UNAUTHORIZED",
  "message": "Invalid email or password",
  "status": 401,
  "path": "/api/v1/auth/login",
  "timestamp": "2024-12-12T10:30:00"
}
```

### Common HTTP Status Codes
- `200 OK` - Request successful
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication failed or token invalid/expired
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource already exists
- `500 Internal Server Error` - Server error

### JWT-Specific Errors

**Invalid Token Signature:**
```json
{
  "error": "UNAUTHORIZED",
  "message": "Invalid token signature",
  "status": 401,
  "path": "/api/v1/orders",
  "timestamp": "2024-12-12T10:30:00"
}
```

**Token Expired:**
```json
{
  "error": "UNAUTHORIZED",
  "message": "Token expired",
  "status": 401,
  "path": "/api/v1/orders",
  "timestamp": "2024-12-12T10:30:00"
}
```

**Malformed Token:**
```json
{
  "error": "UNAUTHORIZED",
  "message": "Malformed or invalid token",
  "status": 401,
  "path": "/api/v1/orders",
  "timestamp": "2024-12-12T10:30:00"
}
```

---

## Authorization Matrix

| Endpoint | Anonymous | Customer | Admin |
|----------|-----------|----------|-------|
| POST /auth/register | ✅ | ✅ | ✅ |
| POST /auth/login | ✅ | ✅ | ✅ |
| POST /auth/password-reset | ✅ | ✅ | ✅ |
| POST /auth/password-reset-check | ✅ | ✅ | ✅ |
| GET /products | ✅ | ✅ | ✅ |
| GET /products/{id} | ✅ | ✅ | ✅ |
| GET /products/search | ✅ | ✅ | ✅ |
| GET /orders | ❌ | ✅ (own) | ✅ (own) |
| GET /orders/{id} | ❌ | ✅ (own) | ✅ (own) |
| POST /orders | ❌ | ✅ | ✅ |
| PUT /orders | ❌ | ✅ (own) | ✅ (own) |
| PATCH /orders/{id} | ❌ | ✅ (own) | ✅ (own) |
| GET /admin/orders | ❌ | ❌ | ✅ |
| POST /admin/products | ❌ | ❌ | ✅ |
| PUT /admin/products | ❌ | ❌ | ✅ |
| DELETE /admin/products/{id} | ❌ | ❌ | ✅ |
| GET /admin/users | ❌ | ❌ | ✅ |
| POST /admin/users | ❌ | ❌ | ✅ |
| PUT /admin/users | ❌ | ❌ | ✅ |
| DELETE /admin/users/{id} | ❌ | ❌ | ✅ |

---

## Settings

The application uses a settings system for configuration values:

- **shipping_fee**: Shipping cost added to all orders (default: "10")
- **tax_rate**: Tax rate (default: "0.22")
- **currency**: Currency code (default: "EUR")
- **store_name**: Store name (default: "Tech & Lifestyle Store")
- **support_email**: Support email (default: "support@ecommerce.com")

Settings are initialized on application startup via `ApplicationConfig.commandLineRunner()`.

---

## Email & OTP System

### Email Configuration (Mailtrap)

The application uses **Mailtrap** for sending emails. Two modes are available:

**Sandbox Mode** (Testing - emails NOT sent):
```properties
mailtrap.token=your_sandbox_token
mailtrap.sandbox=true
mailtrap.inbox.id=your_inbox_id
```

**Production Mode** (Real emails):
```properties
mailtrap.token=your_production_token
mailtrap.sandbox=false
```

### OTP Code System

- **Code Length**: 4 digits
- **Expiration Time**: 10 minutes
- **Generation**: Random numeric code
- **Usage**: Password reset functionality
- **Storage**: Persisted in database with expiration timestamp

**Factory Methods:**
```java
// Default 10-minute expiration
OTPCode.create(char[] code, String email, Long userId)

// Custom expiration time
OTPCode.create(char[] code, String email, Long userId, int minutes)
```

**Validation:**
```java
// Check if code is expired
boolean isExpired()

// Service validation (checks expiration + code match)
boolean isCodeValid(OTPCode code)
```

---

## Security Features

1. **JWT Authentication**: Tokens expire after 1 hour
2. **Password Encryption**: BCrypt with salt rounds
3. **CORS Enabled**: Configured for `http://localhost:5173` (frontend)
4. **CSRF Disabled**: API is stateless
5. **Role-Based Access Control**: `@PreAuthorize` annotations
6. **Custom Exception Handling**: Centralized error responses
7. **Token Validation**: Signature and expiration checks
8. **OTP Security**: Time-limited one-time passwords
9. **Password Hashing**: All passwords encrypted before storage

---

## Development Notes

1. **Database**: H2 in-memory database (auto-recreated on restart)
2. **Data Initialization**: `data.sql` loads test users on startup
3. **Payment Processing**: Currently mocked - all payments succeed after validation
4. **Shipping Fee**: Automatically added to order subtotals
5. **Search**: Case-insensitive partial match on product name
6. **Subtotal Calculation**: Automatically calculated on order creation/update
7. **Email Service**: Mailtrap integration for transactional emails
8. **Password Recovery**: OTP-based system with 10-minute expiration

---

## Test Endpoint

### Test
```http
GET /test/?p={param}
```
**Auth Required:** No

**Query Parameters:**
- `p` (required) - Test parameter

**Response:** `200 OK`
```
Test endpoint. Param = {param}
```

---

## API Changes from Previous Version

### New Features
- ✅ **Password Reset System**: OTP-based password recovery
- ✅ **Email Integration**: Mailtrap for sending emails
- ✅ **Public Product Endpoints**: Products are now accessible without authentication
- ✅ **Enhanced CORS**: Improved configuration for frontend integration
- ✅ **Password Encoding in User Service**: Automatic password hashing on user updates

### Breaking Changes
- ⚠️ **Products endpoints are now PUBLIC**: No authentication required for GET operations
- ⚠️ **Password field handling**: Empty passwords no longer update existing passwords