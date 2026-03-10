package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void testPaymentDetailFormPage() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetailForm"));
    }

    @Test
    void testPaymentDetailRedirect() throws Exception {
        mockMvc.perform(get("/payment/detail").param("paymentId", "p-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/detail/p-1"));
    }

    @Test
    void testPaymentDetailPage() throws Exception {
        Payment payment = new Payment("p-1", "o-1", "BANK_TRANSFER", Map.of(
                "bankName", "BCA",
                "referenceCode", "REF"
        ));
        when(paymentService.getPayment("p-1")).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/p-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"))
                .andExpect(model().attribute("payment", payment));
    }

    @Test
    void testPaymentAdminListPage() throws Exception {
        Payment payment = new Payment("p-1", "o-1", "BANK_TRANSFER", Map.of(
                "bankName", "BCA",
                "referenceCode", "REF"
        ));
        when(paymentService.getAllPayments()).thenReturn(List.of(payment));

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testPaymentAdminDetailPage() throws Exception {
        Payment payment = new Payment("p-1", "o-1", "BANK_TRANSFER", Map.of(
                "bankName", "BCA",
                "referenceCode", "REF"
        ));
        when(paymentService.getPayment("p-1")).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/p-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminDetail"))
                .andExpect(model().attribute("payment", payment));
    }

    @Test
    void testSetPaymentStatus() throws Exception {
        Payment payment = new Payment("p-1", "o-1", "BANK_TRANSFER", Map.of(
                "bankName", "BCA",
                "referenceCode", "REF"
        ));
        when(paymentService.getPayment("p-1")).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/p-1").param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/p-1"));

        verify(paymentService, times(1)).setStatus(eq(payment), eq("SUCCESS"));
    }

    @Test
    void testSetPaymentStatusWhenPaymentNotFound() throws Exception {
        when(paymentService.getPayment("p-404")).thenReturn(null);

        mockMvc.perform(post("/payment/admin/set-status/p-404").param("status", "REJECTED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/p-404"));

        verify(paymentService, times(0)).setStatus(any(Payment.class), any(String.class));
    }
}
