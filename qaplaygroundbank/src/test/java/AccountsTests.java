import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.AccountsPage.AccountButton;
import pages.AccountsPage.AccountElement;
import pages.AccountsPage.AccountHistory;
import pages.AccountsPage.AccountType;

public class AccountsTests extends BaseTests {

    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");
        accountsPage.clickAccountButton(AccountButton.SIDEBAR_ACCOUNTS);
    }


    // Accounts Page
    @Test
    public void testAccountsPageDisplayed() {
        Assert.assertTrue(
                accountsPage.isAccountsPageDisplayed(),
                "Accounts page is not displayed."
        );
    }


    // Account Details
    @DataProvider(name = "accountDetails")
    public Object[][] accountDetails() {
        return new Object[][] {
            {
                AccountElement.LIST_NAME,
                AccountElement.DETAIL_NAME
            },
            {
                AccountElement.LIST_TYPE,
                AccountElement.DETAIL_TYPE
            },
            {
                AccountElement.LIST_BALANCE,
                AccountElement.DETAIL_BALANCE
            }
        };
    }

    @Test(dataProvider = "accountDetails")
    public void testAccountDetails(
            AccountElement listElement,
            AccountElement detailElement) {

        AccountHistory history = accountsPage.getAccountHistory(
                listElement,
                detailElement
        );

        Assert.assertEquals(
                history.getDetailValue(),
                history.getListValue(),
                "Account information does not match."
        );
    }


    // Add Account
    @Test
    public void testAddNewAccount() {
        String accountName = "Art E";
        String accountBalance = "2210.50";

        accountsPage.clickAccountButton(AccountButton.ADD_ACCOUNT);

        accountsPage.createAccount(
                accountName,
                AccountType.CREDIT,
                accountBalance
        );

        List<String> allAccounts = accountsPage.getAllAccountRows();

        System.out.println("Current accounts on page:");
        allAccounts.forEach(System.out::println);

        boolean accountFound = allAccounts.stream()
                .anyMatch(row -> {
                    if (!row.contains(accountName)
                            || !row.contains("Credit")) {
                        return false;
                    }

                    String normalizedBalance =
                            row.replaceAll("[^0-9.]", "");

                    return normalizedBalance.contains(accountBalance);
                });

        Assert.assertTrue(
                accountFound,
                "Newly added account '" + accountName
                        + "' was not found in the accounts list."
        );
    }


    // Delete Account
    @Test
    public void testDeleteAccountButton() {
        accountsPage.clickDeleteAccount("Everyday Checking");
    }

    @Test
    public void testCancelDeleteAccount() {
        String accountName = "Everyday Checking";

        accountsPage.cancelDeleteAccount(accountName);

        Assert.assertTrue(
                accountsPage.isAccountListed(accountName),
                "Account should still exist after cancelling deletion."
        );
    }

    @Test
    public void testConfirmDeleteAccount() {
        String accountName = "Everyday Checking";

        accountsPage.confirmDeleteAccount(accountName);

        Assert.assertFalse(
                accountsPage.isAccountListed(accountName),
                "Account should be removed after confirming deletion."
        );
    }
}