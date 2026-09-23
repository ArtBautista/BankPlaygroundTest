package pages;

import org.openqa.selenium.By;

import base.BasePage;

public class DashboardPage extends BasePage {

    private static final By WELCOME_MESSAGE = By.cssSelector("[data-testid='dashboard-welcome-message']");
    private static final By LOGOUT_BUTTON = By.cssSelector("[data-testid='topbar-logout-btn']");
    private static final By NOTIFICATION_BUTTON = By.cssSelector("[data-testid='nav-notifications-link']");

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

    public enum PageTitle {
        NOTIFICATIONS("notifications-page-title"),
        TRANSFER("transfer-page-title"),
        ACCOUNTS("accounts-page-title"),
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

    public void clickNotificationButton() {
        click(NOTIFICATION_BUTTON);
    }

    public void clickLogoutButton() {
        click(LOGOUT_BUTTON);
    }

    public boolean isWelcomeMessageDisplayed() {
        return isDisplayed(WELCOME_MESSAGE);
    }

    public boolean isPageTitleDisplayed(PageTitle title) {
        return isDisplayed(title.locator());
    }
}