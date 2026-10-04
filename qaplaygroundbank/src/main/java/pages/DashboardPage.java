package pages;

import org.openqa.selenium.By;

import base.BasePage;

public class DashboardPage extends BasePage {

    private static final By WELCOME_MESSAGE = By.cssSelector("[data-testid='dashboard-welcome-message']");
    private static final By LOGOUT_BUTTON = By.cssSelector("[data-testid='topbar-logout-btn']");
    private static final By NOTIFICATION_BUTTON = By.cssSelector("[data-testid='nav-notifications-link']");
    
    public enum QuickAction {
        TRANSFER("quick-action-transfer","/bank/transfer"),
        SEND_MONEY("quick-action-send-money","/bank/send-money"),
        BILL_PAY("quick-action-bill-pay","/bank/bill-pay"),
        APPLY_LOAN("quick-action-apply-loan","/bank/apply-loan"),
        TRANSACTIONS("quick-action-transactions","/bank/transactions");

        private final String testId;
        private final String currentUrl;

        QuickAction(String testId,String currentUrl) {
            this.testId = testId;
            this.currentUrl = currentUrl;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
        public String getUrl() {
            return currentUrl;
        } 
    }

    public enum PageTitle {
        NOTIFICATIONS("notifications-page-title"),
        TRANSFER("transfer-page-title"),
        ACCOUNTS("accounts-page-title"),
        SEND_MONEY("send-money-page-title"),
        BILL_PAY("bill-pay-page-title"),
        TRANSACTIONS("transactions-page-title"),
        APPLY_LOAN("apply-loan-title");

        private final String testId;

        PageTitle(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }

    public void clickQuickAction(QuickAction action) {
        click(action.locator());
    }
    public void clickAccountsButton(){
        click(By.cssSelector("[data-testid='sidebar-link-accounts']"));
    }
    public boolean isPageTitleDisplayed(PageTitle title) {
        return isDisplayed(title.locator());
    }

    public void clickNotificationButton() {
        click(NOTIFICATION_BUTTON);
    }

    public void clickLogoutButton() {
        click(LOGOUT_BUTTON);
    }

    public boolean isWelcomeMessageDisplayed() {
        return isDisplayed(WELCOME_MESSAGE);
    }

    
}