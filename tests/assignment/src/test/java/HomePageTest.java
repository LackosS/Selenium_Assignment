import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class HomePageTest {
    private WebDriver driver;
    private HomePage homePage;

    @Before
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
        driver.manage().window().maximize();
        driver.get("https://www.demoblaze.com/");
        homePage = new HomePage(driver);
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
        assertTrue(homePage.waitAndReturnElement(By.id("footc")).isDisplayed());
        assertNotNull("Navigation bar should be present", homePage.waitAndReturnElement(By.id("navbarExample")));
    }

    @Test
    public void testHomePageToCartAndBack() {
        homePage.navigateHome();
        homePage.navigateToCart();
        assertTrue(driver.getCurrentUrl().contains("cart.html"));
        homePage.navigateBackHome();
        assertTrue(driver.getCurrentUrl().contains("index.html"));
    }

    @Test
    public void testAddSingleProductToCart() {
        homePage.navigateHome();
        homePage.navigateToProduct("Samsung galaxy s6");
        homePage.addToCart();
        homePage.verifyProductInCart("Samsung galaxy s6");
    }

    @Test
    public void testProductDescription() {
        homePage.navigateToProduct("Samsung galaxy s6");
        assertTrue("Description should contain specific keywords", 
            homePage.waitAndReturnElement(By.id("more-information")).findElement(By.tagName("p")).getText().contains("The Samsung Galaxy S6 is powered by"));
    }

    @Test
    public void testNavigationBetweenCategories() {
        homePage.navigateHome();
        
        homePage.navigateToCategory("Phones");
        assertTrue(homePage.waitAndReturnElement(By.linkText("Samsung galaxy s6")).isDisplayed());

        homePage.navigateToCategory("Laptops");
        assertTrue(homePage.waitAndReturnElement(By.linkText("Sony vaio i5")).isDisplayed());

        homePage.navigateToCategory("Monitors");
        assertTrue(homePage.waitAndReturnElement(By.linkText("Apple monitor 24")).isDisplayed());
    }
}
