package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import base.BasePage;


public class LoginPage extends BasePage {
    

    private By usernameField = By.id("login-username");
    private By passwordField = By.id("login-password");
    private By loginButton = By.cssSelector("[data-testid='login-submit-btn']");
    private By forgotPasswordLink = By.xpath("//*[@data-testid='back-to-login-link']");
    private By errorMessage = By.xpath("//*[@data-testid='login-error-message']");

    
    public void setUsername(String username){
        set(usernameField, username);
    }

    public void setPassword(String password){
        set(passwordField, password);
    }

    public DashboardPage clickLoginButton(){
        click(loginButton);
        return new DashboardPage();
    }

    public DashboardPage logIntoApplication(String username, String password){
        setUsername(username);
        setPassword(password);
        
        return clickLoginButton();
    }

    public String getErrorMessage() {
        WebElement error = wait.until(
            ExpectedConditions.visibilityOfElementLocated(errorMessage)
        );
        return error.getText();
    }
}
