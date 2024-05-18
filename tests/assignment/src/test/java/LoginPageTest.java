import java.net.MalformedURLException;
import java.net.URL;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginPageTest {
    private WebDriver driver;
    private LoginPage loginPage;

    @Before
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
        driver.manage().window().maximize();
        driver.get("https://www.demoblaze.com/");
        loginPage = new LoginPage(driver);
    }

    @After
    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSuccessfulLogin() {
        loginPage.initiateLogin();
        loginPage.enterCredentials("your_username", "your_password");
        loginPage.submitLogin();
        assertTrue(loginPage.isLogoutDisplayed());
        loginPage.logout();
    }

    @Test
    public void testInvalidCredentials() {
        loginPage.initiateLogin();
        loginPage.enterCredentials("invalid_username", "your_password");
        loginPage.submitLogin();
        assertEquals("Wrong password.", loginPage.getAlertText());
    }

    @Test
    public void testEmptyCredentials() {
        loginPage.initiateLogin();
        loginPage.submitLogin();
        assertEquals("Please fill out Username and Password.", loginPage.getAlertText());
    }
}