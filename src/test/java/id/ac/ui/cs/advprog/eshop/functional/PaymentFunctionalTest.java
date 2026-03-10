package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createPaymentAndManageStatus_isCorrect(ChromeDriver driver) {
        String author = "Author" + System.currentTimeMillis();

        driver.get(baseUrl + "/order/create");
        driver.findElement(By.id("authorInput")).sendKeys(author);
        driver.findElement(By.id("productNameInput")).sendKeys("Mouse");
        driver.findElement(By.id("quantityInput")).sendKeys("1");
        driver.findElement(By.tagName("button")).click();

        driver.get(baseUrl + "/order/history");
        driver.findElement(By.id("authorInput")).sendKeys(author);
        driver.findElement(By.tagName("button")).click();
        driver.findElement(By.linkText("Pay")).click();

        Select selectMethod = new Select(driver.findElement(By.id("methodInput")));
        selectMethod.selectByValue("BANK_TRANSFER");
        driver.findElement(By.id("bankNameInput")).sendKeys("BCA");
        driver.findElement(By.id("referenceCodeInput")).sendKeys("REF123");
        driver.findElement(By.tagName("button")).click();

        String resultText = driver.getPageSource();
        String paymentId = extractPaymentId(resultText);
        assertNotNull(paymentId);

        driver.get(baseUrl + "/payment/detail/" + paymentId);
        assertTrue(driver.getPageSource().contains(paymentId));

        driver.get(baseUrl + "/payment/admin/list");
        assertTrue(driver.getPageSource().contains(paymentId));

        driver.get(baseUrl + "/payment/admin/detail/" + paymentId);
        driver.findElement(By.cssSelector("button[value='SUCCESS']")).click();

        driver.get(baseUrl + "/payment/detail/" + paymentId);
        assertTrue(driver.getPageSource().contains("SUCCESS"));
    }

    private String extractPaymentId(String pageSource) {
        Pattern pattern = Pattern.compile("Payment ID: ([a-zA-Z0-9\\-]+)");
        Matcher matcher = pattern.matcher(pageSource);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
