import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPageTest {
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
    public void testSuccessfulLogin() {
        driver.findElement(By.id("login2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));
        driver.findElement(By.id("loginusername")).sendKeys("your_username");
        driver.findElement(By.id("loginpassword")).sendKeys("your_password");
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2"))).isDisplayed());
        driver.findElement(By.id("logout2")).click();
    }

    @Test
    public void testInvalidCredentials() throws InterruptedException {
        driver.findElement(By.id("login2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));
        driver.findElement(By.id("loginusername")).sendKeys("invalid_username");
        driver.findElement(By.id("loginpassword")).sendKeys("your_password");
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Wrong password.", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
    }

    @Test
    public void testEmptyCredentials() {
        driver.findElement(By.id("login2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Please fill out Username and Password.", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
    }
}


