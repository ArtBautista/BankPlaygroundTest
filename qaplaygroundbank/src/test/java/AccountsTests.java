import java.time.Duration;
import java.util.List;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.AccountsPage;
import pages.AccountsPage.AccountButton;
import pages.AccountsPage.AccountType;

public class AccountsTests extends BaseTests {

    @BeforeMethod
    public void loginAsStandardUser() {
        loginPage.logIntoApplication("standard_user", "bank_sauce");
        accountsPage.clickAccountButton(AccountButton.SIDEBAR_ACCOUNTS);

        new WebDriverWait(driver, Duration.ofSeconds(10))
        .until(ExpectedConditions.urlContains("/accounts"));
    }


    // Accounts Page
    @Test
    public void testAccountsPageDisplayed() {
        Assert.assertTrue(
                accountsPage.isAccountsPageDisplayed(),
                "Accounts page is not displayed."
        );
    }
    // TC-ACC-001 - Verify Accounts List Load
    @Test
    public void testAccountsListed() {
        
        List<String> allAccounts = accountsPage.getAllAccountRows();
        System.out.println("Accounts on page: ");
        allAccounts.forEach(System.out::println);

        boolean hasChecking = accountsPage.isAccountElementDisplayed(AccountsPage.AccountType.CHECKING.toString().toLowerCase());
        boolean hasSavings = accountsPage.isAccountElementDisplayed(AccountsPage.AccountType.SAVINGS.toString().toLowerCase());
        

        Assert.assertTrue(hasChecking, "No checking account found. Rows: " + allAccounts);
        Assert.assertTrue(hasSavings, "No savings account found. Rows: " + allAccounts);
    }
    //TC-ACC-002 - Verify Account Details Display
    @Test
    public void testAccountDetailsDisplay() {
        String accountName = "Everyday Checking";
        String accountNumber = accountsPage.getMaskedAccountNumber(accountName);

        System.out.println("Account Name: " + accountName);
        System.out.println("Account Number: " + accountNumber);

        Assert.assertTrue(
                accountsPage.isAccountElementDisplayed(accountName),
                "Account name is not visible."
        );

        Assert.assertTrue(
                accountNumber.matches("\\*{4}\\d{4}"),
                "Account number is not properly masked: " + accountNumber
        );

        accountsPage.viewAccount(accountName);
    }
    //TC-ACC-003 - Verify Overdraft Banner
    @Test
    public void testOverdraftBanner() {
        dashboardPage.clickLogoutButton();
        loginPage.logIntoApplication("overdraft_user", "bank_sauce");
        accountsPage.clickAccountButton(AccountButton.SIDEBAR_ACCOUNTS);

        List<String> allAccounts = accountsPage.getAllAccountRows();
        System.out.println("Accounts on page:");
        allAccounts.forEach(System.out::println);

        Assert.assertTrue(
            accountsPage.isAccountElementDisplayed("Overdrawn"),
            "Overdraft warning indicator was not shown on the checking account."
        );
    }
    //TC-ACC-004 - Verify Frozen Account Alert
    @Test
    public void testFrozenAccountAlert() {
        dashboardPage.clickLogoutButton();
        loginPage.logIntoApplication("frozen_user", "bank_sauce");
        accountsPage.clickAccountButton(AccountButton.SIDEBAR_ACCOUNTS);

        
        Assert.assertTrue(
            accountsPage.isFreezeBannerDisplayed(),
            "Freeze Banner is not displayed."
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
                accountsPage.isAccountElementDisplayed(accountName),
                "Account should still exist after cancelling deletion."
        );
    }

    @Test
    public void testConfirmDeleteAccount() {
        String accountName = "Everyday Checking";

        accountsPage.confirmDeleteAccount(accountName);

        Assert.assertFalse(
                accountsPage.isAccountElementDisplayed(accountName),
                "Account should be removed after confirming deletion."
        );
    }




}