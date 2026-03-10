package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    @Autowired
    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/create")
    public String createOrderPage(Model model) {
        model.addAttribute("orderForm", new OrderForm());
        return "createOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(@ModelAttribute OrderForm orderForm) {
        Product product = new Product();
        product.setProductName(orderForm.getProductName());
        product.setProductQuantity(orderForm.getProductQuantity());

        List<Product> products = new ArrayList<>();
        products.add(product);

        Order order = new Order(
                UUID.randomUUID().toString(),
                products,
                System.currentTimeMillis(),
                orderForm.getAuthor()
        );
        orderService.createOrder(order);
        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String orderHistoryPage() {
        return "orderHistory";
    }

    @PostMapping("/history")
    public String orderHistoryPost(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
        return "orderList";
    }

    @GetMapping("/pay/{orderId}")
    public String orderPayPage(@PathVariable("orderId") String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "payOrder";
    }

    @PostMapping("/pay/{orderId}")
    public String orderPayPost(
            @PathVariable("orderId") String orderId,
            @RequestParam("method") String method,
            @RequestParam Map<String, String> requestParams,
            Model model
    ) {
        Order order = orderService.findById(orderId);

        Map<String, String> paymentData = new HashMap<>();
        if ("VOUCHER_CODE".equals(method)) {
            paymentData.put("voucherCode", requestParams.getOrDefault("voucherCode", ""));
        } else if ("BANK_TRANSFER".equals(method)) {
            paymentData.put("bankName", requestParams.getOrDefault("bankName", ""));
            paymentData.put("referenceCode", requestParams.getOrDefault("referenceCode", ""));
        }

        Payment payment = paymentService.addPayment(order, method, paymentData);
        model.addAttribute("payment", payment);
        model.addAttribute("paymentId", payment.getId());
        return "payOrderResult";
    }

    public static class OrderForm {
        private String author;
        private String productName;
        private int productQuantity;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getProductQuantity() {
            return productQuantity;
        }

        public void setProductQuantity(int productQuantity) {
            this.productQuantity = productQuantity;
        }
    }
}
