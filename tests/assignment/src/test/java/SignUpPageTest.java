import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SignUpPageTest {
    private WebDriver driver;
    private SignUpPage signUpPage;

    @Before
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
        driver.manage().window().maximize();
        driver.get("https://www.demoblaze.com/");
        signUpPage = new SignUpPage(driver);
    }

    @After
    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSuccessfulSignUp() {
        String uniqueUsername = "user_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        signUpPage.navigateToSignUp();
        signUpPage.enterCredentials(uniqueUsername, "testpassword");
        signUpPage.submitSignUp();
        assertEquals("Sign up successful.", signUpPage.getAlertText());
    }

    @Test
    public void testUnsuccessfulSignUp() {
        signUpPage.navigateToSignUp();
        signUpPage.enterCredentials("testuser", "testpassword");
        signUpPage.submitSignUp();
        assertEquals("This user already exist.", signUpPage.getAlertText());
    }

    @Test
    public void testEmptySignUp() {
        signUpPage.navigateToSignUp();
        signUpPage.enterCredentials("", "");
        signUpPage.submitSignUp();
        assertEquals("Please fill out Username and Password.", signUpPage.getAlertText());
    }
}
