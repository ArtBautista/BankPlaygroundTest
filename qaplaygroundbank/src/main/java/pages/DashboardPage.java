package pages;

import java.math.BigDecimal;

import org.openqa.selenium.By;

import base.BasePage;

public class DashboardPage extends BasePage {

    private static final By WELCOME_MESSAGE = By.cssSelector("[data-testid='dashboard-welcome-message']");
    private static final By LOGOUT_BUTTON = By.cssSelector("[data-testid='topbar-logout-btn']");
    private static final By NOTIFICATION_BUTTON = By.cssSelector("[data-testid='nav-notifications-link']");
    private static final By TOTAL_NET_WORTH = By.cssSelector("[data-testid='stat-card-net-worth-value']");

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
        APPLY_LOAN("apply-loan-page-title");

        private final String testId;

        PageTitle(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }
    public enum Sidebar {
        DASHBOARD("sidebar-link-dashboard"),
        ACCOUNTS("sidebar-link-accounts"),
        TRANSFER("sidebar-link-transfer"),
        SEND_MONEY("sidebar-link-send-money"),
        BILL_PAY("sidebar-link-bill-pay"),
        TRANSACTIONS("sidebar-link-transactions"),
        APPLY_LOAN("sidebar-link-apply-loan"),
        NOTIFICATION("sidebar-link-notification"),
        PROFILE("sidebar-link-profile");

        private final String testId;

        Sidebar(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }
    public void clickQuickAction(QuickAction action) {
        click(action.locator());
    }
    public void clickSideBarButton(Sidebar action){
        click(action.locator());
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
    
    public BigDecimal getNetWorth() {
        String netWorth = find(TOTAL_NET_WORTH).getText();

        String normalizedNetWorth = netWorth
                .replace("$", "")
                .replace(",", "")
                .trim();

        return new BigDecimal(normalizedNetWorth);
    }
    
    public boolean isWelcomeMessageDisplayed() {
        return isDisplayed(WELCOME_MESSAGE);
    }

    
}