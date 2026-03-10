package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createOrder"))
                .andExpect(model().attributeExists("orderForm"));
    }

    @Test
    void testCreateOrderPost() throws Exception {
        mockMvc.perform(post("/order/create")
                        .param("author", "tester")
                        .param("productName", "Book")
                        .param("productQuantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/order/history"));

        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    void testOrderHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"));
    }

    @Test
    void testOrderHistoryPost() throws Exception {
        Product product = new Product();
        product.setProductName("Book");
        product.setProductQuantity(1);
        Order order = new Order("o-1", List.of(product), 1L, "tester");
        when(orderService.findAllByAuthor("tester")).thenReturn(List.of(order));

        mockMvc.perform(post("/order/history").param("author", "tester"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderList"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("author", "tester"));
    }

    @Test
    void testOrderPayPage() throws Exception {
        Product product = new Product();
        product.setProductName("Book");
        product.setProductQuantity(1);
        Order order = new Order("o-2", List.of(product), 1L, "tester");
        when(orderService.findById("o-2")).thenReturn(order);

        mockMvc.perform(get("/order/pay/o-2"))
                .andExpect(status().isOk())
                .andExpect(view().name("payOrder"))
                .andExpect(model().attribute("order", order));
    }

    @Test
    void testOrderPayPost() throws Exception {
        Product product = new Product();
        product.setProductName("Book");
        product.setProductQuantity(1);
        Order order = new Order("o-3", List.of(product), 1L, "tester");
        when(orderService.findById("o-3")).thenReturn(order);

        Payment payment = new Payment("p-1", "o-3", "BANK_TRANSFER", Map.of(
                "bankName", "BCA",
                "referenceCode", "REF123"
        ));
        when(paymentService.addPayment(eq(order), eq("BANK_TRANSFER"), any(Map.class))).thenReturn(payment);

        mockMvc.perform(post("/order/pay/o-3")
                        .param("method", "BANK_TRANSFER")
                        .param("bankName", "BCA")
                        .param("referenceCode", "REF123"))
                .andExpect(status().isOk())
                .andExpect(view().name("payOrderResult"))
                .andExpect(model().attribute("paymentId", "p-1"));

        verify(paymentService, times(1)).addPayment(eq(order), eq("BANK_TRANSFER"), any(Map.class));
    }
}
