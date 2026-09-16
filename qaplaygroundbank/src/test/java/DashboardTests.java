
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
    public void testLogoutButton(){
        loginPage.logIntoApplication("standard_user","bank_sauce");
        dashboardPage.clickLogoutButton();     
    }
    
    @Test 
    public void testTransferMoneyQA(){
        loginPage.logIntoApplication("standard_user","bank_sauce");
        dashboardPage.clickQuickAction(DashboardPage.QuickAction.TRANSFER);
    }

    @Test 
    public void testSendMoneyQA(){
        loginPage.logIntoApplication("standard_user","bank_sauce");
        dashboardPage.clickQuickAction(DashboardPage.QuickAction.SEND_MONEY);
    }

    @Test 
    public void testBillPayQA(){
        loginPage.logIntoApplication("standard_user","bank_sauce");
        dashboardPage.clickQuickAction(DashboardPage.QuickAction.BILL_PAY);
    }

    @Test 
    public void testApplyLoanQA(){
        loginPage.logIntoApplication("standard_user","bank_sauce");
        dashboardPage.clickQuickAction(DashboardPage.QuickAction.APPLY_LOAN);
    }

    @Test 
    public void testTransactionsQA(){
        loginPage.logIntoApplication("standard_user","bank_sauce");
        dashboardPage.clickQuickAction(DashboardPage.QuickAction.TRANSACTIONS);
    }
    
}
