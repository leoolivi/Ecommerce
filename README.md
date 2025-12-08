# E-Commerce API Documentation

## Base URL
```
http://localhost:8080/api/v1
```

## Authentication
Currently disabled - all endpoints are publicly accessible.

---

## Products

### Get All Products
```http
GET /products
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Product Name",
    "description": "Product description",
    "price": 18.99,
    "stockQuantity": 10,
    "category": "Category 1",
    "imageUrl": ""
  }
]
```

### Get Product by ID
```http
GET /product/{id}
```

**Parameters:**
- `id` (path) - Product ID

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Product Name",
  "description": "Product description",
  "price": 18.99,
  "stockQuantity": 10,
  "category": "Category 1",
  "imageUrl": ""
}
```

**Errors:**
- `404 Not Found` - Product not found

### Search Products
```http
GET /search/products?q={query}
```

**Query Parameters:**
- `q` (required) - Search query string

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Matching Product",
    "description": "Description",
    "price": 18.99,
    "stockQuantity": 10,
    "category": "Category 1",
    "imageUrl": ""
  }
]
```

### Add Product
```http
POST /products
```

**Request Body:**
```json
{
  "name": "New Product",
  "description": "Product description",
  "price": 29.99,
  "stockQuantity": 50,
  "category": "Category Name",
  "imageUrl": "https://example.com/image.jpg"
}
```

**Response:** `200 OK`
```json
"Product added successfully"
```

### Update Product
```http
PUT /products
```

**Request Body:**
```json
{
  "id": 1,
  "name": "Updated Name",
  "description": "Updated description",
  "price": 24.99,
  "stockQuantity": 30,
  "category": "Updated Category",
  "imageUrl": "https://example.com/new-image.jpg"
}
```

**Response:** `200 OK`
```json
"Product updated successfully"
```

### Delete Product
```http
DELETE /products/{id}
```

**Parameters:**
- `id` (path) - Product ID

**Response:** `200 OK`
```json
"Product deleted successfully"
```

---

## Orders

### Get All Orders
```http
GET /orders
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "products": [...],
    "subtotal": 28.99,
    "shippingAddress": "123 Main St, City, Country",
    "customerId": 1,
    "payment": {
      "type": "CREDIT_CARD",
      "cardNumber": "1234567890123456",
      "cardHolder": "John Doe",
      "expiryMonth": "12",
      "expiryYear": "2025",
      "cvc": "123"
    },
    "status": "PAID"
  }
]
```

### Get Order by ID
```http
GET /order/{id}
```

**Parameters:**
- `id` (path) - Order ID

**Response:** `200 OK`
```json
{
  "id": 1,
  "products": [...],
  "subtotal": 28.99,
  "shippingAddress": "123 Main St",
  "customerId": 1,
  "payment": {...},
  "status": "PAID"
}
```

**Errors:**
- `404 Not Found` - Order not found

### Create Order
```http
POST /orders
```

**Request Body:**
```json
{
  "productIds": [1, 2],
  "shippingAddress": "123 Main St, City, Country",
  "customerId": 1,
  "paymentMethod": {
    "type": "CREDIT_CARD",
    "cardNumber": "1234567890123456",
    "cardHolder": "John Doe",
    "expiryMonth": "12",
    "expiryYear": "2025",
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
- Subtotal is automatically calculated including shipping fee
- Order status flow: CREATED → PAYING → PAID
- Payment is validated (card number must be at least 12 characters)

### Update Order
```http
PUT /order
```

**Request Body:**
```json
{
  "id": 1,
  "productIds": [1, 3],
  "shippingAddress": "456 New Address",
  "customerId": 1,
  "orderStatus": "SHIPPED"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "products": [...],
  "subtotal": 35.99,
  "shippingAddress": "456 New Address",
  "customerId": 1,
  "payment": {...},
  "status": "SHIPPED"
}
```

**Errors:**
- `404 Not Found` - Order not found

### Delete Order
```http
DELETE /order/{id}
```

**Parameters:**
- `id` (path) - Order ID

**Response:** `200 OK`
```json
"Order deleted successfully"
```

**Errors:**
- `404 Not Found` - Order not found

---

## Data Models

### Product
```typescript
{
  id: number
  name: string          // unique, required
  description: string   // optional
  price: number         // required
  stockQuantity: number // required
  category: string      // required
  imageUrl: string      // required
}
```

### Order
```typescript
{
  id: number
  products: Product[]
  subtotal: number              // auto-calculated
  shippingAddress: string       // required
  customerId: number            // optional
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

### OrderStatus Enum
- `CREATED` - Order has been created
- `PAYING` - Payment is being processed
- `PAID` - Payment successful
- `SHIPPED` - Order has been shipped
- `CANCELED` - Order has been canceled

---

## Settings

The application uses a settings system for configuration values:

- **shipping_fee**: Shipping cost added to all orders (default: "10")

Settings are initialized on application startup and can be modified through the SettingService.

---

## Error Responses

All error responses follow this format:

```json
{
  "timestamp": "2024-12-08T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found",
  "path": "/api/v1/product/999"
}
```

### Common HTTP Status Codes
- `200 OK` - Request successful
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## Test Endpoint

### Test
```http
GET /test/?p={param}
```

**Query Parameters:**
- `p` (required) - Test parameter

**Response:** `200 OK`
```
Test endpoint. Param = {param}
```

---

## Notes

1. **Security**: Authentication is currently disabled. All endpoints are publicly accessible.
2. **Payment Processing**: Currently mocked - all payments return success after validation.
3. **Shipping Fee**: Automatically added to order subtotals from settings.
4. **Product Search**: Case-sensitive partial match on product name.
5. **Subtotal Calculation**: Automatically calculated on order creation/update including shipping fee.
