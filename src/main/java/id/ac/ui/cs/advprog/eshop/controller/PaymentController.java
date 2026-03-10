package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/detail")
    public String paymentDetailForm() {
        return "paymentDetailForm";
    }

    @GetMapping(value = "/detail", params = "paymentId")
    public String paymentDetailRedirect(@RequestParam("paymentId") String paymentId) {
        return "redirect:/payment/detail/" + paymentId;
    }

    @GetMapping("/detail/{paymentId}")
    public String paymentDetailPage(@PathVariable("paymentId") String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "paymentDetail";
    }

    @GetMapping("/admin/list")
    public String paymentAdminListPage(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "paymentAdminList";
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String paymentAdminDetailPage(@PathVariable("paymentId") String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "paymentAdminDetail";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(
            @PathVariable("paymentId") String paymentId,
            @RequestParam("status") String status
    ) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null) {
            paymentService.setStatus(payment, status);
        }
        return "redirect:/payment/admin/detail/" + paymentId;
    }
}
