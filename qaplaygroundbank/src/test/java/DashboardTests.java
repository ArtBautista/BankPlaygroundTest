import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.DashboardPage;
import pages.DashboardPage.PageTitle;
import pages.DashboardPage.QuickAction;

public class DashboardTests extends BaseTests {

    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");
    }
    //TC-DASH-001 - Verify Dashboard Load
    @Test
    public void testDashboardHeaderIsDisplayed() {
        Assert.assertTrue(dashboardPage.isWelcomeMessageDisplayed(),
                "Dashboard welcome message is not displayed");
    }

    @Test
    public void testNotificationButton() {
        dashboardPage.clickNotificationButton();
        Assert.assertTrue(dashboardPage.isPageTitleDisplayed(DashboardPage.PageTitle.NOTIFICATIONS),
                "Notifications page title is not displayed");
    }

    @Test
    public void testLogoutButton() {
        dashboardPage.clickLogoutButton();
        
    }
    //TC-DASH-004 & TC-DASH-005
    @DataProvider(name = "quickActions")
    public Object[][] quickActions() {
        return new Object[][] {
            { QuickAction.TRANSFER, PageTitle.TRANSFER },
            { QuickAction.SEND_MONEY, PageTitle.SEND_MONEY},
            { QuickAction.BILL_PAY, PageTitle.BILL_PAY },
            { QuickAction.APPLY_LOAN, PageTitle.APPLY_LOAN },
            { QuickAction.TRANSACTIONS, PageTitle.TRANSACTIONS }
        };
    }

    @Test(dataProvider = "quickActions")
    public void testQuickAction(QuickAction action) {
        dashboardPage.clickQuickAction(action);

        Assert.assertTrue(
            dashboardPage.isCurrentUrl(action.getUrl()),
            "Expected URL: " + action.getUrl()
            + " | Actual URL: " + driver.getCurrentUrl()
        );

        
        System.out.println("Expected URL: " + action.getUrl());
        System.out.println("Actual URL: " + driver.getCurrentUrl());
    }
    @Test
    public void testTotalAccountBalance() {

        BigDecimal netWorth = dashboardPage.getNetWorth();
        dashboardPage.clickAccountsButton();

        BigDecimal totalBalance = accountsPage.getTotalAccountBalance();

        // Debug
        System.out.println("Net Worth: $" + netWorth);
        System.out.println("Total Account Balance: $" + totalBalance);

        Assert.assertTrue(
                totalBalance.compareTo(netWorth) == 0,
                "Total account balance does not match dashboard net worth. "
                        + "Expected: $" + netWorth
                        + " | Actual: $" + totalBalance
        );
    }
}