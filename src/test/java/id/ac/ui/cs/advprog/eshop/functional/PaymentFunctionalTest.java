package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class PaymentFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createPaymentAndManageStatus_isCorrect(ChromeDriver driver) {
        String orderId = UUID.randomUUID().toString();

        Product product = new Product();
        product.setProductName("Mouse");
        product.setProductQuantity(1);
        Order order = new Order(orderId, List.of(product), System.currentTimeMillis(), "tester");
        orderService.createOrder(order);

        driver.get(baseUrl + "/order/pay/" + orderId);

        Select selectMethod = new Select(driver.findElement(By.id("methodInput")));
        selectMethod.selectByValue("BANK_TRANSFER");
        driver.findElement(By.id("bankNameInput")).sendKeys("BCA");
        driver.findElement(By.id("referenceCodeInput")).sendKeys("REF123");
        driver.findElement(By.tagName("button")).click();

        driver.get(baseUrl + "/payment/admin/list");
        assertTrue(driver.getPageSource().contains("All Payments"));
        driver.findElement(By.linkText("Manage")).click();

        driver.findElement(By.cssSelector("button[value='SUCCESS']")).click();
        assertTrue(driver.getPageSource().contains("SUCCESS"));
    }
}
