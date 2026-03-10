package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String paymentId = UUID.randomUUID().toString();
        String status = "PENDING";

        if ("VOUCHER_CODE".equals(method)) {
            status = validateVoucherCode(paymentData) ? "SUCCESS" : "REJECTED";
        } else if ("BANK_TRANSFER".equals(method)) {
            status = validateBankTransfer(paymentData) ? "PENDING" : "REJECTED";
        }

        Payment payment = new Payment(paymentId, order.getId(), method, paymentData, status);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        Payment savedPayment = paymentRepository.save(payment);

        // Update order status based on payment status
        if ("SUCCESS".equals(status)) {
            orderService.updateStatus(payment.getOrderId(), "SUCCESS");
        } else if ("REJECTED".equals(status)) {
            orderService.updateStatus(payment.getOrderId(), "FAILED");
        }

        return savedPayment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private boolean validateVoucherCode(Map<String, String> paymentData) {
        String voucherCode = paymentData.get("voucherCode");
        if (voucherCode == null || voucherCode.length() != 16) {
            return false;
        }
        if (!voucherCode.startsWith("ESHOP")) {
            return false;
        }
        // Check for 8 numerical characters
        String numericalPart = voucherCode.substring(5);
        if (numericalPart.length() != 11) { // ESHOP (5) + 8 numbers + 3 chars = 16
            return false;
        }
        String numbers = numericalPart.substring(0, 8);
        try {
            Integer.parseInt(numbers);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateBankTransfer(Map<String, String> paymentData) {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");
        return bankName != null && !bankName.trim().isEmpty() &&
               referenceCode != null && !referenceCode.trim().isEmpty();
    }
}