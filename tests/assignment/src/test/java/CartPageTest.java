import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class CartPageTest {
    private WebDriver driver;
    private CartPage cartPage;
    private HomePage homePage; 
    private LoginPage loginPage;
    private String productName = "Samsung galaxy s6";

    @Before
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
        driver.manage().window().maximize();
        driver.get("https://www.demoblaze.com/");
        homePage = new HomePage(driver);
        cartPage = new CartPage(driver);
        loginPage = new LoginPage(driver);
    }

    @After
    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testRemoveProductFromCart() {
        homePage.navigateToProduct(productName);
        homePage.addToCart();
        cartPage.navigateToCart();
        cartPage.removeProduct(productName);
        assertTrue("Product should not be in the cart", !cartPage.isProductDisplayed(productName));
    }

    @Test
    public void testTotalPriceCalculation() {
        homePage.navigateToProduct(productName);
        homePage.addToCart();
        homePage.addToCart();
        cartPage.navigateToCart();
        double pricePerUnit = cartPage.getPricePerUnit(productName);
        double expectedTotal = pricePerUnit * 2;
        double displayedTotal = cartPage.getTotalPrice();
        assertEquals("Total price should be calculated correctly", expectedTotal, displayedTotal, 0.01);
    }

    @Test
    public void testPlaceOrder(){
        homePage.navigateHome();
        loginPage.initiateLogin();
        loginPage.enterCredentials("asd", "asd");
        loginPage.submitLogin();
        assertTrue(loginPage.isLogoutDisplayed());
        homePage.navigateToCart();
        cartPage.placeOrder();
        assertEquals("Thank you for your purchase!",cartPage.verifyPlaceOrderConfirmationMsg());
    }
}
