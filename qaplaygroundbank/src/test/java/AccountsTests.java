
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTests;

public class AccountsTests extends BaseTests {

    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");
        accountsPage.clickSidebarAccountsButton();
    }

    @Test 
    public void testAccountsHeader(){
        Assert.assertTrue(accountsPage.isAccountsHeaderDisplayed(),
                "Accounts message is not displayed");  
    }
    
}
