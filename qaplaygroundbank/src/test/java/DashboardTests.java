
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.DashboardPage;


public class DashboardTests extends BaseTests {

    @Test 
    public void testDashboardHeaderIsDisplayed(){
        DashboardPage dashboardPage = loginPage.logIntoApplication("standard_user","bank_sauce");
        Assert.assertTrue(dashboardPage.getWelcomeMessage(),
        "\n Dashboard is not displayed \n");
        
    }
    
    @Test 
    public void testTransferMoneyQA(){
        loginPage.logIntoApplication("standard_user","bank_sauce");
        dashboardPage.clickQuickAction(DashboardPage.QuickAction.TRANSFER);
    }

    
}
