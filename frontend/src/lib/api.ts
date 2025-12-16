const API_BASE_URL = 'http://localhost:8080/api/v1';

interface AuthResponse {
  accessToken: string;
  expiresIn: number;
  user: {
    id: number;
    email: string;
    role: 'CUSTOMER' | 'ADMIN';
  };
}

class ApiClient {
  private getHeaders(includeAuth = true): HeadersInit {
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
    };

    if (includeAuth) {
      const token = localStorage.getItem('accessToken');
      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }
    }

    return headers;
  }

  private async request<T>(
    endpoint: string,
    options: RequestInit = {},
    includeAuth = true
  ): Promise<T> {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      ...options,
      headers: {
        ...this.getHeaders(includeAuth),
        ...options.headers,
      },
    });

    if (!response.ok) {
      const error = await response.json().catch(() => ({
        message: 'An error occurred',
      }));
      throw new Error(error.message || `Error: ${response.status}`);
    }

    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return response.json();
    }

    return response.text() as T;
  }

  async login(email: string, password: string): Promise<AuthResponse> {
    return this.request<AuthResponse>(
      '/auth/login',
      {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      },
      false
    );
  }

  async register(email: string, password: string): Promise<AuthResponse> {
    return this.request<AuthResponse>(
      '/auth/register',
      {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      },
      false
    );
  }

  async requestPasswordReset(email: string): Promise<string> {
    return this.request<string>(
      '/auth/password-reset',
      {
        method: 'POST',
        body: JSON.stringify({ email }),
      },
      false
    );
  }

  async resetPassword(otp: string, newPassword: string): Promise<string> {
    return this.request<string>(
      '/auth/password-reset-check',
      {
        method: 'POST',
        body: JSON.stringify({ otp, newPassword }),
      },
      false
    );
  }

  async getProducts(): Promise<Product[]> {
    return this.request<Product[]>('/products', {}, false);
  }

  async getProduct(id: number): Promise<Product> {
    return this.request<Product>(`/products/${id}`, {}, false);
  }

  async searchProducts(query: string): Promise<Product[]> {
    return this.request<Product[]>(`/products/search?q=${encodeURIComponent(query)}`, {}, false);
  }

  async getOrders(): Promise<Order[]> {
    return this.request<Order[]>('/orders');
  }

  async getOrder(id: number): Promise<Order> {
    return this.request<Order>(`/orders/${id}`);
  }

  async createOrder(data: CreateOrderRequest): Promise<string> {
    return this.request<string>('/orders', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async updateOrder(data: UpdateOrderRequest): Promise<Order> {
    return this.request<Order>('/orders', {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  async cancelOrder(id: number): Promise<string> {
    return this.request<string>(`/orders/${id}`, {
      method: 'PATCH',
    });
  }

  async adminGetAllOrders(): Promise<Order[]> {
    return this.request<Order[]>('/admin/orders');
  }

  async adminGetAllUsers(): Promise<User[]> {
    return this.request<User[]>('/admin/users');
  }

  async adminCreateProduct(data: ProductInput): Promise<string> {
    return this.request<string>('/admin/products', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async adminUpdateProduct(data: Product): Promise<string> {
    return this.request<string>('/admin/products', {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  async adminDeleteProduct(id: number): Promise<string> {
    return this.request<string>(`/admin/products/${id}`, {
      method: 'DELETE',
    });
  }

  async adminDeleteOrder(id: number): Promise<string> {
    return this.request<string>(`/admin/orders/${id}`, {
      method: 'PUT',
    });
  }

  async adminCreateUser(data: UserInput): Promise<User> {
    return this.request<User>('/admin/users', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async adminDeleteUser(id: number): Promise<void> {
    return this.request<void>(`/admin/users/${id}`, {
      method: 'DELETE',
    });
  }
}

export const api = new ApiClient();

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  stockQuantity: number;
  category: string;
  imageUrl: string;
}

export interface ProductInput {
  name: string;
  description: string;
  price: number;
  stockQuantity: number;
  category: string;
  imageUrl: string;
}

export interface Order {
  id: number;
  products: Product[];
  subtotal: number;
  shippingAddress: string;
  customerId: number;
  payment: PaymentMethod;
  status: OrderStatus;
}

export interface PaymentMethod {
  type: 'CREDIT_CARD' | 'PAYPAL' | 'STRIPE';
  cardNumber: string;
  cardHolder: string;
  expiryMonth: string;
  expiryYear: string;
  cvc: string;
}

export interface CreateOrderRequest {
  productIds: number[];
  shippingAddress: string;
  paymentMethod: PaymentMethod;
}

export interface UpdateOrderRequest {
  id: number;
  productIds: number[];
  shippingAddress: string;
  orderStatus: OrderStatus;
}

export interface User {
  id: number;
  email: string;
  role: 'CUSTOMER' | 'ADMIN';
}

export interface UserInput {
  email: string;
  password: string;
  role: 'CUSTOMER' | 'ADMIN';
}

export type OrderStatus = 'CREATED' | 'PAYING' | 'PAID' | 'SHIPPED' | 'CANCELED';

export type { AuthResponse };
