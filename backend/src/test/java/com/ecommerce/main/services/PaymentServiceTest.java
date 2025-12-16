// ========================================
// PaymentServiceTest.java
// ========================================
package com.ecommerce.main.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.main.models.Order;
import com.ecommerce.main.models.PaymentMethod;
import com.ecommerce.main.models.Product;
import com.ecommerce.main.models.enums.OrderStatus;
import com.ecommerce.main.models.enums.PaymentType;

@ExtendWith(MockitoExtension.class)
@DisplayName("Payment Service Tests")
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    private Order testOrder;
    private PaymentMethod validPayment;
    private PaymentMethod invalidPayment;
    private PaymentMethod shortCardPayment;

    @BeforeEach
    void setUp() {
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(99.99)
                .build();

        validPayment = new PaymentMethod(
                PaymentType.CREDIT_CARD,
                "1234567890123456",
                "John Doe",
                "12",
                "2026",
                "123"
        );

        invalidPayment = new PaymentMethod(
                PaymentType.CREDIT_CARD,
                "123",
                "John Doe",
                "12",
                "2026",
                "123"
        );

        shortCardPayment = new PaymentMethod(
                PaymentType.CREDIT_CARD,
                "12345678901",
                "John Doe",
                "12",
                "2026",
                "123"
        );

        testOrder = Order.builder()
                .id(1L)
                .products(Arrays.asList(product))
                .subtotal(109.99)
                .shippingAddress("Test Address")
                .customerId(1L)
                .payment(validPayment)
                .status(OrderStatus.PAYING)
                .build();
    }

    @Test
    @DisplayName("Should process payment successfully with valid card")
    void testProcessPaymentSuccess() {
        // Act
        boolean result = paymentService.processPayment(testOrder);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should throw exception with invalid card number (too short)")
    void testProcessPaymentInvalidCard() {
        // Arrange
        testOrder.setPayment(invalidPayment);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.processPayment(testOrder);
        });
    }

    @Test
    @DisplayName("Should throw exception with card number less than 12 digits")
    void testProcessPaymentShortCard() {
        // Arrange
        testOrder.setPayment(shortCardPayment);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.processPayment(testOrder);
        });
    }

    @Test
    @DisplayName("Should validate card number length correctly")
    void testPaymentMethodValidation() {
        // Act & Assert
        assertDoesNotThrow(() -> validPayment.validate());
        assertThrows(IllegalArgumentException.class, () -> invalidPayment.validate());
        assertThrows(IllegalArgumentException.class, () -> shortCardPayment.validate());
    }

    @Test
    @DisplayName("Should mask card number correctly")
    void testCardNumberMasking() {
        // Act
        String masked = validPayment.mask();

        // Assert
        assertEquals("**** **** **** 3456", masked);
    }

    @Test
    @DisplayName("Should mask short card number")
    void testShortCardNumberMasking() {
        // Arrange
        PaymentMethod shortCard = new PaymentMethod(
                PaymentType.CREDIT_CARD,
                "123",
                "John Doe",
                "12",
                "2026",
                "123"
        );

        // Act
        String masked = shortCard.mask();

        // Assert
        assertEquals("****", masked);
    }

    @Test
    @DisplayName("Should handle null card number in masking")
    void testNullCardNumberMasking() {
        // Arrange
        PaymentMethod nullCard = new PaymentMethod(
                PaymentType.CREDIT_CARD,
                null,
                "John Doe",
                "12",
                "2026",
                "123"
        );

        // Act
        String masked = nullCard.mask();

        // Assert
        assertEquals("****", masked);
    }

    @Test
    @DisplayName("Should process payment for different payment types")
    void testProcessPaymentDifferentTypes() {
        // Arrange
        PaymentMethod paypalPayment = new PaymentMethod(
                PaymentType.PAYPAL,
                "1234567890123456",
                "John Doe",
                "12",
                "2026",
                "123"
        );
        testOrder.setPayment(paypalPayment);

        // Act
        boolean result = paymentService.processPayment(testOrder);

        // Assert
        assertTrue(result);
    }
}