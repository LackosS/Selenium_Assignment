import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

public class SignUpPageTest {
    private WebDriver driver;
    private WebDriverWait wait;

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
    public void testSuccessfulSignUp() {
        String uniqueUsername = "user_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        driver.findElement(By.id("signin2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signInModal")));
        driver.findElement(By.id("sign-username")).sendKeys(uniqueUsername);
        driver.findElement(By.id("sign-password")).sendKeys("testpassword");
        driver.findElement(By.xpath("//button[text()='Sign up']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Sign up successful.", driver.switchTo().alert().getText());
    }

    @Test
    public void testUnsuccessfulSignUp() {
        driver.findElement(By.id("signin2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signInModal")));
        driver.findElement(By.id("sign-username")).sendKeys("testuser");
        driver.findElement(By.id("sign-password")).sendKeys("testpassword");
        driver.findElement(By.xpath("//button[text()='Sign up']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("This user already exist.", driver.switchTo().alert().getText());
    }

    @Test
    public void testEmptySignUp() {
        driver.findElement(By.id("signin2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signInModal")));
        driver.findElement(By.xpath("//button[text()='Sign up']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Please fill out Username and Password.", driver.switchTo().alert().getText());
    }
}