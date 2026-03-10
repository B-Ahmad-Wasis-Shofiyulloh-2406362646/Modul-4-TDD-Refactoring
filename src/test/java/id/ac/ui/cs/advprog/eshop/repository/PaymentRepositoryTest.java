package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {

    PaymentRepository paymentRepository;
    Payment payment1;
    Payment payment2;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP123456789012");
        payment1 = new Payment("payment-123", "order-456", "VOUCHER_CODE", paymentData1, "SUCCESS");

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("bankName", "BCA");
        paymentData2.put("referenceCode", "REF123");
        payment2 = new Payment("payment-456", "order-789", "BANK_TRANSFER", paymentData2, "PENDING");
    }

    @Test
    void testSaveCreate() {
        Payment result = paymentRepository.save(payment1);

        Payment findResult = paymentRepository.findById(payment1.getId());
        assertEquals(payment1.getId(), result.getId());
        assertEquals(payment1.getId(), findResult.getId());
        assertEquals(payment1.getOrderId(), findResult.getOrderId());
        assertEquals(payment1.getMethod(), findResult.getMethod());
        assertEquals(payment1.getStatus(), findResult.getStatus());
        assertEquals(payment1.getPaymentData(), findResult.getPaymentData());
    }

    @Test
    void testSaveUpdate() {
        paymentRepository.save(payment1);
        Payment updatedPayment = new Payment(payment1.getId(), payment1.getOrderId(), payment1.getMethod(),
                payment1.getPaymentData(), "REJECTED");
        Payment result = paymentRepository.save(updatedPayment);

        Payment findResult = paymentRepository.findById(payment1.getId());
        assertEquals(payment1.getId(), result.getId());
        assertEquals(payment1.getId(), findResult.getId());
        assertEquals(payment1.getOrderId(), findResult.getOrderId());
        assertEquals(payment1.getMethod(), findResult.getMethod());
        assertEquals("REJECTED", findResult.getStatus());
    }

    @Test
    void testFindByIdIfIdFound() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        Payment findResult = paymentRepository.findById(payment1.getId());
        assertEquals(payment1.getId(), findResult.getId());
        assertEquals(payment1.getOrderId(), findResult.getOrderId());
        assertEquals(payment1.getMethod(), findResult.getMethod());
        assertEquals(payment1.getStatus(), findResult.getStatus());
    }

    @Test
    void testFindByIdIfIdNotFound() {
        paymentRepository.save(payment1);

        Payment findResult = paymentRepository.findById("nonexistent");
        assertNull(findResult);
    }

    @Test
    void testFindAll() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> payments = paymentRepository.findAll();
        assertEquals(2, payments.size());
        assertTrue(payments.contains(payment1));
        assertTrue(payments.contains(payment2));
    }

    @Test
    void testFindAllEmpty() {
        List<Payment> payments = paymentRepository.findAll();
        assertTrue(payments.isEmpty());
    }
}