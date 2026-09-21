
package base;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BasePage {
    
    public static WebDriver driver;
    public static WebDriverWait wait;

    public void setDriver(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    }

    protected WebElement find(By locator){
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void set(By locator, String text){
        WebElement el = waitForVisible(locator);
        el.clear();
        el.sendKeys(text);

    }

    protected void click(By locator){
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public static void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exc) {
            exc.printStackTrace();
        }
    }

    protected boolean isDisplayed(By locator) {
    try {
        return find(locator).isDisplayed();
    } catch (Exception e) {
        return false;
    }
}
}
