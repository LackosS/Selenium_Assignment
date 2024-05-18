import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends PageBase {
    By loginButton = By.id("login2");
    By usernameField = By.id("loginusername");
    By passwordField = By.id("loginpassword");
    By loginSubmitButton = By.xpath("//button[text()='Log in']");
    By logoutButton = By.id("logout2");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void initiateLogin() {
        waitAndReturnElement(loginButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
    }

    public void enterCredentials(String username, String password) {
        waitAndReturnElement(usernameField).sendKeys(username);
        waitAndReturnElement(passwordField).sendKeys(password);
    }

    public void submitLogin() {
        waitAndReturnElement(loginSubmitButton).click();
    }

    public boolean isLogoutDisplayed() {
        return waitAndReturnElement(logoutButton).isDisplayed();
    }

    public void logout() {
        waitAndReturnElement(logoutButton).click();
    }

    public String getAlertText() {
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert().getText();
    }
}
