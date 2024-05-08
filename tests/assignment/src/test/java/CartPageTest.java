import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPageTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private String productName = "Samsung galaxy s6";

    @Before
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10);
        driver.get("https://www.demoblaze.com/");
    }

    @After
    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testRemoveProductFromCart() {
        navigateToProduct(productName);
        addToCart();
        verifyProductInCart(productName);
        driver.findElement(By.xpath("//tr[td[contains(text(),'" + productName + "')]]/td/a[contains(text(),'Delete')]"))
                .click();
        List<WebElement> products = driver.findElements(By.linkText(productName));
        boolean isPresent = products.stream().anyMatch(WebElement::isDisplayed);
        assertTrue("Product should not be in the cart", !isPresent);
    }

    @Test
    public void testTotalPriceCalculation() {
        navigateToProduct(productName);
        addToCart();
        addToCart();
        verifyMultipleProductsInCart(new String[]{productName, productName});
        String unitPriceText = driver.findElement(By.xpath("//tr[td[contains(text(),'" + productName + "')]]/td[3]"))
                .getText();
        double pricePerUnit = Double.parseDouble(unitPriceText.replace("$", "").trim());
        double expectedTotal = pricePerUnit * 2;
        String displayedTotalText = driver.findElement(By.id("totalp")).getText();
        double displayedTotal = Double.parseDouble(displayedTotalText.replace("$", "").trim());
        assertEquals("Total price should be calculated correctly", expectedTotal, displayedTotal, 0.01);
    }

    private void navigateToProduct(String productName) {
        driver.findElement(By.cssSelector("a.nav-link[href='index.html']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Phones")));
        driver.findElement(By.linkText("Phones")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(productName)));
        List<WebElement> links = driver.findElements(By.className("hrefch"));
        for (WebElement link : links) {
            if (link.getText().equals(productName)) {
                link.click();
                break;
            }
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.btn.btn-success.btn-lg")));
    }

    private void addToCart() {
        driver.findElement(By.cssSelector("a.btn.btn-success.btn-lg")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Product added", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
    }

    private void verifyProductInCart(String productName) {
        driver.findElement(By.id("cartur")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='" + productName + "']")));
        assertTrue("Product should be in the cart",
                driver.findElement(By.xpath("//td[text()='" + productName + "']")).isDisplayed());
    }
    private void verifyMultipleProductsInCart(String[] productNames) {
        driver.findElement(By.id("cartur")).click();
        for (String productName : productNames) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='" + productName + "']")));
            assertTrue("Product should be in the cart", driver.findElement(By.xpath("//td[text()='" + productName + "']")).isDisplayed());
        }
    }
}
