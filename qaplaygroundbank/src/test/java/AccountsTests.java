
import java.util.List;

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
            accountsPage.clickAccountButtons(AccountButtons.VIEW_ACTIONS_BUTTON);
            String detailValue = accountsPage.checkAccountDetails(details);
            System.out.println("DETAILS: " + detailValue);
            Assert.assertEquals(detailValue, listValue);
    }
    //Add New Account Test
    @Test
    public void testAddNewAccount() {
        accountsPage.clickAccountButtons(AccountButtons.ADD_ACTIONS_BUTTON);
        accountsPage.createAccount("Art E", AccountsPage.AccountType.CREDIT_TYPE, "2210.50");

        List<String> allAccounts = accountsPage.getAllAccountRows();

        // Display all content in the list
        System.out.println("Current accounts on page:");
        allAccounts.forEach(System.out::println);

        // Assert the newly added account is present
        boolean found = allAccounts.stream()
        .anyMatch(row -> {
            if (!row.contains("Art E") || !row.contains("Credit")) {
                return false;
            }
            // extract numeric value from the row, ignoring $ and commas
            String normalized = row.replaceAll("[^0-9.]", "");
            return normalized.contains("2210.50");
        });

        Assert.assertTrue(found, "Newly added account 'Art E' was not found in the accounts list");
    }
}
