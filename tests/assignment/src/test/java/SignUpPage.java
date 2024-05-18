import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SignUpPage extends PageBase {
    By signInButton = By.id("signin2");
    By signInModal = By.id("signInModal");
    By usernameField = By.id("sign-username");
    By passwordField = By.id("sign-password");
    By signUpButton = By.xpath("//button[text()='Sign up']");

    public SignUpPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToSignUp() {
        waitAndReturnElement(signInButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(signInModal));
    }

    public void enterCredentials(String username, String password) {
        waitAndReturnElement(usernameField).sendKeys(username);
        waitAndReturnElement(passwordField).sendKeys(password);
    }

    public void submitSignUp() {
        waitAndReturnElement(signUpButton).click();
        wait.until(ExpectedConditions.alertIsPresent());
    }

    public String getAlertText() {
        return driver.switchTo().alert().getText();
    }
}
