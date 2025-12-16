// ========================================
// OrderServiceTest.java
// ========================================
package com.ecommerce.main.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.naming.OperationNotSupportedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.main.data.OrderRequestDTO;
import com.ecommerce.main.exceptions.OrderNotFoundException;
import com.ecommerce.main.exceptions.SettingNotFoundException;
import com.ecommerce.main.models.Order;
import com.ecommerce.main.models.PaymentMethod;
import com.ecommerce.main.models.Product;
import com.ecommerce.main.models.enums.OrderStatus;
import com.ecommerce.main.models.enums.PaymentType;
import com.ecommerce.main.repositories.OrderRepository;
import com.ecommerce.main.repositories.ProductRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Order Service Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SettingService settingService;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;
    private Product testProduct;
    private PaymentMethod testPayment;
    private OrderRequestDTO testRequest;

    @BeforeEach
    void setUp() throws SettingNotFoundException {
        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(99.99)
                .stockQuantity(10)
                .category("Electronics")
                .imageUrl("test.jpg")
                .build();

        testPayment = new PaymentMethod(
                PaymentType.CREDIT_CARD,
                "1234567890123456",
                "John Doe",
                "12",
                "2026",
                "123"
        );

        testOrder = Order.builder()
                .id(1L)
                .products(Arrays.asList(testProduct))
                .subtotal(109.99)
                .shippingAddress("Test Address")
                .customerId(1L)
                .payment(testPayment)
                .status(OrderStatus.CREATED)
                .build();

        testRequest = OrderRequestDTO.builder()
                .productIds(Arrays.asList(1L))
                .shippingAddress("Test Address")
                .customerId(1L)
                .paymentMethod(testPayment)
                .build();

        when(settingService.getValueByKey("shipping_fee")).thenReturn("10");
    }

    @Test
    @DisplayName("Should get all orders")
    void testGetOrders() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(Arrays.asList(testOrder));

        // Act
        List<Order> orders = orderService.getOrders();

        // Assert
        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find order by ID")
    void testFindOrderById() throws OrderNotFoundException {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // Act
        Order found = orderService.findOrderById(1L);

        // Assert
        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when order not found")
    void testFindOrderByIdNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.findOrderById(999L);
        });
    }

    @Test
    @DisplayName("Should get orders of customer")
    void testGetOrdersOfCustomer() throws OrderNotFoundException {
        // Arrange
        when(orderRepository.findAllByCustomerId(1L))
                .thenReturn(Optional.of(Arrays.asList(testOrder)));

        // Act
        List<Order> orders = orderService.getOrdersOfCustomer(1L);

        // Assert
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getCustomerId());
        verify(orderRepository, times(1)).findAllByCustomerId(1L);
    }

    @Test
    @DisplayName("Should throw exception when customer has no orders")
    void testGetOrdersOfCustomerNotFound() {
        // Arrange
        when(orderRepository.findAllByCustomerId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrdersOfCustomer(999L);
        });
    }

    @Test
    @DisplayName("Should find order by ID for specific customer")
    void testFindOrderByIdOfCustomer() throws OrderNotFoundException, OperationNotSupportedException {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // Act
        Order found = orderService.findOrderByIdOfCustomer(1L, 1L);

        // Assert
        assertNotNull(found);
        assertEquals(1L, found.getCustomerId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when accessing another customer's order")
    void testFindOrderByIdOfCustomerUnauthorized() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // Act & Assert
        assertThrows(OperationNotSupportedException.class, () -> {
            orderService.findOrderByIdOfCustomer(1L, 2L);
        });
    }

    @Test
    @DisplayName("Should add order successfully")
    void testAddOrder() throws SettingNotFoundException {
        // Arrange
        when(productRepository.findAllById(anyList())).thenReturn(Arrays.asList(testProduct));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        Order created = orderService.addOrder(testRequest);

        // Assert
        assertNotNull(created);
        verify(productRepository, times(1)).findAllById(anyList());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should update order successfully")
    void testUpdateOrder() throws OrderNotFoundException, SettingNotFoundException {
        // Arrange
        OrderRequestDTO updateRequest = OrderRequestDTO.builder()
                .id(1L)
                .productIds(Arrays.asList(1L))
                .shippingAddress("New Address")
                .customerId(1L)
                .orderStatus(OrderStatus.SHIPPED)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(productRepository.findAllById(anyList())).thenReturn(Arrays.asList(testProduct));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        Order updated = orderService.updateOrder(updateRequest);

        // Assert
        assertNotNull(updated);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should update order of customer successfully")
    void testUpdateOrderOfCustomer() throws OrderNotFoundException, SettingNotFoundException, OperationNotSupportedException {
        // Arrange
        OrderRequestDTO updateRequest = OrderRequestDTO.builder()
                .id(1L)
                .productIds(Arrays.asList(1L))
                .shippingAddress("New Address")
                .orderStatus(OrderStatus.SHIPPED)
                .build();

        when(orderRepository.findByCustomerId(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(productRepository.findAllById(anyList())).thenReturn(Arrays.asList(testProduct));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        Order updated = orderService.updateOrderOfCustomer(updateRequest, 1L);

        // Assert
        assertNotNull(updated);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw exception when updating another customer's order")
    void testUpdateOrderOfCustomerUnauthorized() {
        // Arrange
        OrderRequestDTO updateRequest = OrderRequestDTO.builder()
                .id(1L)
                .productIds(Arrays.asList(1L))
                .shippingAddress("New Address")
                .build();

        when(orderRepository.findByCustomerId(2L)).thenReturn(Optional.of(testOrder));

        // Act & Assert
        assertThrows(OperationNotSupportedException.class, () -> {
            orderService.updateOrderOfCustomer(updateRequest, 2L);
        });
    }

    @Test
    @DisplayName("Should update order status")
    void testUpdateOrderStatus() throws OrderNotFoundException {
        // Arrange
        OrderRequestDTO statusRequest = OrderRequestDTO.builder()
                .id(1L)
                .orderStatus(OrderStatus.PAID)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        orderService.updateOrderStatus(statusRequest);

        // Assert
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should cancel order of customer")
    void testCancelOrderOfCustomer() throws OrderNotFoundException, OperationNotSupportedException {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        orderService.cancelOrderOfCustomer(1L, 1L);

        // Assert
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw exception when canceling another customer's order")
    void testCancelOrderOfCustomerUnauthorized() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // Act & Assert
        assertThrows(OperationNotSupportedException.class, () -> {
            orderService.cancelOrderOfCustomer(1L, 2L);
        });
    }

    @Test
    @DisplayName("Should delete order")
    void testDeleteOrder() throws OrderNotFoundException {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        doNothing().when(orderRepository).deleteById(1L);

        // Act
        orderService.deleteOrder(1L);

        // Assert
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent order")
    void testDeleteOrderNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.deleteOrder(999L);
        });
    }
}