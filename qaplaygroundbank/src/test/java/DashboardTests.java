import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.DashboardPage;
import pages.DashboardPage.QuickAction;

public class DashboardTests extends BaseTests {

    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");
    }

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

    @DataProvider(name = "quickActions")
    public Object[][] quickActions() {
        return new Object[][] {
            { QuickAction.TRANSFER },
            { QuickAction.SEND_MONEY },
            { QuickAction.BILL_PAY },
            { QuickAction.APPLY_LOAN },
            { QuickAction.TRANSACTIONS }
        };
    }

    @Test(dataProvider = "quickActions")
    public void testQuickAction(QuickAction action) {
        dashboardPage.clickQuickAction(action);
    }
}