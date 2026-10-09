
import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.DashboardPage.PageTitle;
import pages.DashboardPage.QuickAction;
import pages.DashboardPage.Sidebar;

public class DashboardTests extends BaseTests {

    private WebDriverWait wait;

    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='dashboard-welcome-message']")
        ));
    }

    // TC-DASH-001 - Verify Dashboard Load
    @Test
    public void testDashboardHeaderIsDisplayed() {
        Assert.assertTrue(
                dashboardPage.isWelcomeMessageDisplayed(),
                "Dashboard welcome message is not displayed."
        );
    }

    // TC-DASH-002 - Verify Total Account Balance
    @Test
    public void testTotalAccountBalance() {

        BigDecimal netWorth = dashboardPage.getNetWorth();

        dashboardPage.clickSideBarButton(Sidebar.ACCOUNTS);

        wait.until(ExpectedConditions.urlContains("/bank/accounts"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='account-row']")
        ));

        BigDecimal totalBalance =
                accountsPage.getTotalAccountBalance();

        System.out.println("Dashboard Net Worth: $" + netWorth);
        System.out.println("Total Account Balance: $" + totalBalance);

        Assert.assertEquals(
                totalBalance.compareTo(netWorth),
                0,
                "Total account balance does not match dashboard net worth."
                        + " Expected: $" + netWorth
                        + " | Actual: $" + totalBalance
        );
    }

    // TC-DASH-004 & TC-DASH-005 - Verify Quick Action Navigation
    @DataProvider(name = "quickActions")
    public Object[][] quickActions() {
        return new Object[][] {
            { QuickAction.TRANSFER, PageTitle.TRANSFER },
            { QuickAction.SEND_MONEY, PageTitle.SEND_MONEY },
            { QuickAction.BILL_PAY, PageTitle.BILL_PAY },
            { QuickAction.APPLY_LOAN, PageTitle.APPLY_LOAN },
            { QuickAction.TRANSACTIONS, PageTitle.TRANSACTIONS }
        };
    }

    @Test(dataProvider = "quickActions")
    public void testQuickAction(
            QuickAction action,
            PageTitle expectedTitle) {

        dashboardPage.clickQuickAction(action);

        wait.until(ExpectedConditions.urlContains(action.getUrl()));

        String actualUrl = driver.getCurrentUrl();
        String actualPath = URI.create(actualUrl).getPath();

        System.out.println("Quick Action: " + action);
        System.out.println("Expected URL: " + action.getUrl());
        System.out.println("Actual URL: " + actualUrl);
        System.out.println("Expected Page Title: " + expectedTitle);
        System.out.println("===============================");
        Assert.assertEquals(
                actualPath,
                action.getUrl(),
                "Quick Action navigated to an incorrect URL."
        );

        Assert.assertTrue(
                dashboardPage.isPageTitleDisplayed(expectedTitle),
                "Expected page title is not displayed: " + expectedTitle
        );

        
    }

    // TC-DASH-006 - Verify Notification Navigation
    @Test
    public void testNotificationButton() {

        dashboardPage.clickNotificationButton();

        wait.until(ExpectedConditions.urlContains(
                "/bank/notifications"
        ));

        Assert.assertTrue(
                dashboardPage.isPageTitleDisplayed(PageTitle.NOTIFICATIONS),
                "Notifications page title is not displayed."
        );
    }

    // TC-DASH-007 - Verify Logout Functionality
    @Test
    public void testLogoutButton() {

        dashboardPage.clickLogoutButton();

        wait.until(ExpectedConditions.urlContains("/bank/login"));

        Assert.assertEquals(
                URI.create(driver.getCurrentUrl()).getPath(),
                "/bank/login",
                "User was not redirected to the login page."
        );
    }
}
