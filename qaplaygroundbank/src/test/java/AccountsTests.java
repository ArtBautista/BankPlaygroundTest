
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.AccountsPage;
import pages.AccountsPage.AccountButtons;

public class AccountsTests extends BaseTests {

    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");
        accountsPage.clickAccountButtons(AccountButtons.SIDEBAR_ACCOUNTS_BUTTON);
    }

    @Test 
    public void testAccountsHeader(){
        Assert.assertTrue(accountsPage.isAccountsHeaderDisplayed(),
                "Accounts message is not displayed");  
    }

    @DataProvider(name = "accountCredentials")
    public Object[][] accountCredentials() {
        return new Object[][] {
            { AccountsPage.AccountInformation.ACCOUNT_NAME, AccountsPage.AccountDetails.ACCOUNT_DETAILS_NAME },
            { AccountsPage.AccountInformation.ACCOUNT_TYPE, AccountsPage.AccountDetails.ACCOUNT_DETAILS_TYPE },
            { AccountsPage.AccountInformation.ACCOUNT_BALANCE, AccountsPage.AccountDetails.ACCOUNT_DETAILS_BALANCE }
        };
    }
   
    @Test(dataProvider = "accountCredentials")
    public void testAccountCredentials(AccountsPage.AccountInformation information, AccountsPage.AccountDetails details) {
        String listValue = accountsPage.checkAccountInformation(information);
        System.out.println("INFORMATION: " + listValue);
        accountsPage.clickAccountButtons(AccountsPage.AccountButtons.VIEW_ACTIONS_BUTTON);
        String detailValue = accountsPage.checkAccountDetails(details);
        System.out.println("DETAILS: " + detailValue);
        Assert.assertEquals(detailValue, listValue);
    }


    
}
