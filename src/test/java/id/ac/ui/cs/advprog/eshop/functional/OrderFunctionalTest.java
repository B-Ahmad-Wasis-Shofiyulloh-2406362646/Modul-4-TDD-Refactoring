package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.beans.factory.annotation.Autowired;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class OrderFunctionalTest {

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
    void orderCreatePage_isAccessible(ChromeDriver driver) {
        driver.get(baseUrl + "/order/create");
        assertTrue(driver.getPageSource().contains("Create Order"));
    }

    @Test
    void searchOrderHistory_isCorrect(ChromeDriver driver) {
        String author = "Author" + System.currentTimeMillis();
        String orderId = UUID.randomUUID().toString();

        Product product = new Product();
        product.setProductName("Notebook");
        product.setProductQuantity(2);
        Order order = new Order(orderId, List.of(product), System.currentTimeMillis(), author);
        orderService.createOrder(order);

        driver.get(baseUrl + "/order/history");
        driver.findElement(By.id("authorInput")).sendKeys(author);
        driver.findElement(By.tagName("button")).click();

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Order List"));
        assertTrue(pageSource.contains(orderId));
    }
}
