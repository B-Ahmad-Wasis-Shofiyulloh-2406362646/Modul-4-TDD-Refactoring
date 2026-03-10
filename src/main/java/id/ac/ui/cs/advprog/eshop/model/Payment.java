package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import java.util.Map;

@Getter
public class Payment {
    String id;
    String orderId;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String orderId, String method, Map<String, String> paymentData) {
        // TODO: implement
        this.id = id;
        this.orderId = orderId;
        this.method = method;
        this.paymentData = paymentData;
    }

    public Payment(String id, String orderId, String method, Map<String, String> paymentData, String status) {
        // TODO: implement
        this.id = id;
        this.orderId = orderId;
        this.method = method;
        this.status = status;
        this.paymentData = paymentData;
    }

    public void setStatus(String status) {
        // TODO: implement
    }
}