import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends PageBase {
    By cartLink = By.id("cartur");
    By totalPrice = By.id("totalp");
    By placeOrderButton = By.xpath("//*[@id='page-wrapper']//button[text()='Place Order']");
    By placeOrderModal = By.id("orderModal");
    By placeOrderNameField = By.id("name");
    By placeOrderCardField = By.id("card");
    By purchaseModalButton = By.xpath("//button[text()='Purchase']");
    By confirmationModal = By.cssSelector(".sweet-alert.showSweetAlert.visible");
    By confirmationTitle = By.cssSelector(".sweet-alert.showSweetAlert.visible h2");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToCart() {
        waitAndReturnElement(cartLink).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Products']")));
    }

    public void removeProduct(String productName) {
        By deleteLink = By.xpath("//tr[td[contains(text(),'" + productName + "')]]/td/a[contains(text(),'Delete')]");
        waitAndReturnElement(deleteLink).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//tr[td[contains(text(),'" + productName + "')]]")));
    }

    public boolean isProductDisplayed(String productName) {
        List<WebElement> products = driver.findElements(By.linkText(productName));
        return products.stream().anyMatch(WebElement::isDisplayed);
    }

    public double getTotalPrice() {
        String displayedTotalText = waitAndReturnElement(totalPrice).getText();
        return Double.parseDouble(displayedTotalText.replace("$", "").trim());
    }

    public double getPricePerUnit(String productName) {
        By unitPrice = By.xpath("//tr[td[contains(text(),'" + productName + "')]]/td[3]");
        String unitPriceText = waitAndReturnElement(unitPrice).getText();
        return Double.parseDouble(unitPriceText.replace("$", "").trim());
    }
    public void placeOrder(){
        waitAndReturnElement(placeOrderButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(placeOrderModal));
        wait.until(ExpectedConditions.visibilityOfElementLocated(placeOrderNameField));
        wait.until(ExpectedConditions.visibilityOfElementLocated(placeOrderCardField));
        waitAndReturnElement(placeOrderNameField).sendKeys("Order");
        waitAndReturnElement(placeOrderCardField).sendKeys("Card");
        wait.until(ExpectedConditions.elementToBeClickable(purchaseModalButton));
        waitAndReturnElement(purchaseModalButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(confirmationModal));        
    }

    public String verifyPlaceOrderConfirmationMsg(){
        return waitAndReturnElement(confirmationTitle).getText();
    }
}
