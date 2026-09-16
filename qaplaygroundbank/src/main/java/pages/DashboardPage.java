package pages;

import org.openqa.selenium.By;

import base.BasePage;

public class DashboardPage extends BasePage {
        
    private By welcomeMessage = By.xpath("//*[@data-testid='dashboard-welcome-message']");
    private final By transferMoneyButton = By.cssSelector("[data-testid='quick-action-transfer']");
    private final By sendMoneyButton = By.cssSelector("[data-testid='quick-action-send-money']");
    private final By billPayButton = By.cssSelector("[data-testid='quick-action-bill-pay']");
    private final By applyLoanButton = By.cssSelector("[data-testid='quick-action-apply-loan']");
    private final By transactionsButton = By.cssSelector("[data-testid='quick-action-transactions']");


    public enum QuickAction {
        TRANSFER("quick-action-transfer"),
        SEND_MONEY("quick-action-send-money"),
        BILL_PAY("quick-action-bill-pay"),
        APPLY_LOAN("quick-action-apply-loan"),
        TRANSACTIONS("quick-action-transactions");

        private final String testId;

        QuickAction(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }

    public void clickQuickAction(QuickAction action) {
        click(action.locator());
    }

     public boolean  getWelcomeMessage(){
        return find(welcomeMessage).isDisplayed();
    }
    
}
