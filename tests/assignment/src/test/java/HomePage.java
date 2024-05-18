import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends PageBase {
    By currentLink = By.xpath("//a[@class='nav-link' and @href='index.html' and contains(span, 'current')]");
    By cartLink = By.id("cartur");
    By phonesCategoryLink = By.linkText("Phones");
    By laptopsCategoryLink = By.linkText("Laptops");
    By monitorsCategoryLink = By.linkText("Monitors");
    By addToCartButton = By.cssSelector("a.btn.btn-success.btn-lg");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void navigateHome() {
        waitAndReturnElement(currentLink).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("CATEGORIES")));
    }

    public void navigateToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink));
        waitAndReturnElement(cartLink).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//h2[text()='Products']"))));
    }

    public void navigateBackHome() {
        driver.navigate().back();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("CATEGORIES")));
    }

    public void navigateToCategory(String categoryLinkText) {
        waitAndReturnElement(By.linkText(categoryLinkText)).click();
    }

    public void navigateToProduct(String productName) {
        navigateToCategory("Phones");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(productName)));
        List<WebElement> links = driver.findElements(By.className("hrefch"));
        wait.until(ExpectedConditions.visibilityOfAllElements(links));
        for (WebElement link : links) {
            if (link.getText().equals(productName)) {
                wait.until(ExpectedConditions.elementToBeClickable(link));
                link.click();
                break;
            }
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartButton));
    }

    public void addToCart() {
        waitAndReturnElement(addToCartButton).click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    public void verifyProductInCart(String productName) {
        navigateToCart();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='" + productName + "']")));
    }
}
