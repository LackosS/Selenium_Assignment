import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HomePageTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
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
    public void testHomePageStaticElements() {
        assertEquals("STORE", driver.getTitle());
        assertTrue(driver.findElement(By.id("footc")).isDisplayed());
        WebElement navbar = driver.findElement(By.id("navbarExample"));
        assertNotNull("Navigation bar should be present", navbar);
    }

    @Test
    public void testHomePageToCartAndBack(){
        driver.findElement(By.xpath("//a[@class='nav-link' and @href='index.html' and contains(span, 'current')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("CATEGORIES")));
        assertTrue(driver.getCurrentUrl().contains("index.html"));
        driver.findElement(By.id("cartur")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//h2[text()='Products']"))));
        assertTrue(driver.getCurrentUrl().contains("cart.html"));
        driver.navigate().back();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("CATEGORIES")));
        assertTrue(driver.getCurrentUrl().contains("index.html"));
    }

    @Test
    public void testAddSingleProductToCart() {
        navigateToProduct("Samsung galaxy s6");
        addToCart();
        verifyProductInCart("Samsung galaxy s6");
    }

    @Test
    public void testAddMultipleProductsToCart() {
        navigateToProduct("Samsung galaxy s6");
        addToCart();
        navigateToProduct("Samsung galaxy s6");
        addToCart();
        verifyMultipleProductsInCart(new String[]{"Samsung galaxy s6", "Samsung galaxy s6"});
    }

    @Test
    public void testProductDescription() {
        navigateToProduct("Samsung galaxy s6");
        WebElement moreInfoTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("more-information")));
        WebElement descriptionParagraph = moreInfoTab.findElement(By.tagName("p"));
        String productDescription = descriptionParagraph.getText();
        assertTrue("Description should contain specific keywords", productDescription.contains("The Samsung Galaxy S6 is powered by"));
    }

    @Test
    public void testNavigationBetweenCategories() {
        driver.findElement(By.linkText("Phones")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Samsung galaxy s6")));
        assertTrue(driver.findElement(By.linkText("Samsung galaxy s6")).isDisplayed());

        driver.findElement(By.linkText("Laptops")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Sony vaio i5")));
        assertTrue(driver.findElement(By.linkText("Sony vaio i5")).isDisplayed());

        driver.findElement(By.linkText("Monitors")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Apple monitor 24")));
        assertTrue(driver.findElement(By.linkText("Apple monitor 24")).isDisplayed());
    }

    private void navigateToProduct(String productName) {
        driver.findElement(By.xpath("//a[@class='nav-link' and @href='index.html' and contains(span, 'current')]")).click();
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
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='btn btn-success btn-lg' and contains(@onclick, 'addToCart(1)')]")));
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
        assertTrue("Product should be in the cart", driver.findElement(By.xpath("//td[text()='" + productName + "']")).isDisplayed());
    }

    private void verifyMultipleProductsInCart(String[] productNames) {
        driver.findElement(By.id("cartur")).click();
        for (String productName : productNames) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='" + productName + "']")));
            assertTrue("Product should be in the cart", driver.findElement(By.xpath("//td[text()='" + productName + "']")).isDisplayed());
        }
    }
}
