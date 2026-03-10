package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        this.paymentData = new HashMap<>();
        this.paymentData.put("voucherCode", "ESHOP123456789012");
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment("payment-123", "order-456", "VOUCHER_CODE", this.paymentData);

        assertEquals("payment-123", payment.getId());
        assertEquals("order-456", payment.getOrderId());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        assertEquals(this.paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentWithStatus() {
        Payment payment = new Payment("payment-123", "order-456", "VOUCHER_CODE", this.paymentData, "SUCCESS");

        assertEquals("payment-123", payment.getId());
        assertEquals("order-456", payment.getOrderId());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(this.paymentData, payment.getPaymentData());
    }

    @Test
    void testSetStatus() {
        Payment payment = new Payment("payment-123", "order-456", "VOUCHER_CODE", this.paymentData);
        payment.setStatus("SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(null, "order-456", "VOUCHER_CODE", this.paymentData);
        });
    }

    @Test
    void testCreatePaymentNullOrderId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", null, "VOUCHER_CODE", this.paymentData);
        });
    }

    @Test
    void testCreatePaymentNullMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", "order-456", null, this.paymentData);
        });
    }

    @Test
    void testCreatePaymentNullPaymentData() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", "order-456", "VOUCHER_CODE", null);
        });
    }

    @Test
    void testCreatePaymentWithStatusNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(null, "order-456", "VOUCHER_CODE", this.paymentData, "SUCCESS");
        });
    }

    @Test
    void testCreatePaymentWithStatusNullOrderId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", null, "VOUCHER_CODE", this.paymentData, "SUCCESS");
        });
    }

    @Test
    void testCreatePaymentWithStatusNullMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", "order-456", null, this.paymentData, "SUCCESS");
        });
    }

    @Test
    void testCreatePaymentWithStatusNullPaymentData() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", "order-456", "VOUCHER_CODE", null, "SUCCESS");
        });
    }
}