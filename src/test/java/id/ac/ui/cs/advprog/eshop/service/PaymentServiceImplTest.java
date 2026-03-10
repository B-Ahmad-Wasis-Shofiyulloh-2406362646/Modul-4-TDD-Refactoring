package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderService orderService;

    Order order;
    Map<String, String> voucherPaymentData;
    Map<String, String> bankTransferPaymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");

        voucherPaymentData = new HashMap<>();
        voucherPaymentData.put("voucherCode", "ESHOP123456789012");

        bankTransferPaymentData = new HashMap<>();
        bankTransferPaymentData.put("bankName", "BCA");
        bankTransferPaymentData.put("referenceCode", "REF123");
    }

    @Test
    void testAddPaymentVoucherValid() {
        Payment expectedPayment = new Payment("payment-123", order.getId(), "VOUCHER_CODE", voucherPaymentData, "SUCCESS");
        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", voucherPaymentData);

        assertNotNull(result);
        assertEquals("VOUCHER_CODE", result.getMethod());
        assertEquals("SUCCESS", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherInvalid() {
        Map<String, String> invalidVoucherData = new HashMap<>();
        invalidVoucherData.put("voucherCode", "INVALID");

        Payment expectedPayment = new Payment("payment-123", order.getId(), "VOUCHER_CODE", invalidVoucherData, "REJECTED");
        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", invalidVoucherData);

        assertNotNull(result);
        assertEquals("VOUCHER_CODE", result.getMethod());
        assertEquals("REJECTED", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentBankTransferValid() {
        Payment expectedPayment = new Payment("payment-123", order.getId(), "BANK_TRANSFER", bankTransferPaymentData, "PENDING");
        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", bankTransferPaymentData);

        assertNotNull(result);
        assertEquals("BANK_TRANSFER", result.getMethod());
        assertEquals("PENDING", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentBankTransferInvalid() {
        Map<String, String> invalidBankData = new HashMap<>();
        invalidBankData.put("bankName", null); // null bankName

        Payment expectedPayment = new Payment("payment-123", order.getId(), "BANK_TRANSFER", invalidBankData, "REJECTED");
        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", invalidBankData);

        assertNotNull(result);
        assertEquals("BANK_TRANSFER", result.getMethod());
        assertEquals("REJECTED", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = new Payment("payment-123", order.getId(), "VOUCHER_CODE", voucherPaymentData, "PENDING");
        Payment updatedPayment = new Payment("payment-123", order.getId(), "VOUCHER_CODE", voucherPaymentData, "SUCCESS");
        when(paymentRepository.save(any(Payment.class))).thenReturn(updatedPayment);

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderService, times(1)).updateStatus(order.getId(), "SUCCESS");
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = new Payment("payment-123", order.getId(), "VOUCHER_CODE", voucherPaymentData, "PENDING");
        Payment updatedPayment = new Payment("payment-123", order.getId(), "VOUCHER_CODE", voucherPaymentData, "REJECTED");
        when(paymentRepository.save(any(Payment.class))).thenReturn(updatedPayment);

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderService, times(1)).updateStatus(order.getId(), "FAILED");
    }

    @Test
    void testGetPayment() {
        Payment expectedPayment = new Payment("payment-123", order.getId(), "VOUCHER_CODE", voucherPaymentData, "SUCCESS");
        when(paymentRepository.findById("payment-123")).thenReturn(expectedPayment);

        Payment result = paymentService.getPayment("payment-123");

        assertEquals(expectedPayment, result);
        verify(paymentRepository, times(1)).findById("payment-123");
    }

    @Test
    void testGetAllPayments() {
        List<Payment> expectedPayments = new ArrayList<>();
        expectedPayments.add(new Payment("payment-123", order.getId(), "VOUCHER_CODE", voucherPaymentData, "SUCCESS"));
        when(paymentRepository.findAll()).thenReturn(expectedPayments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(expectedPayments, result);
        verify(paymentRepository, times(1)).findAll();
    }
}