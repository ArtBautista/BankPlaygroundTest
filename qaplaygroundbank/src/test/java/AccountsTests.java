
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTests;
import pages.AccountsPage.AccountButton;
import pages.AccountsPage.AccountElement;
import pages.AccountsPage.AccountType;

public class AccountsTests extends BaseTests {

    private static final String PASSWORD = "bank_sauce";
    private static final String DEFAULT_USER = "standard_user";
    private static final String CHECKING_ACCOUNT = "Everyday Checking";

    private WebDriverWait wait;

    @BeforeMethod
    public void setUpAccounts() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void loginAndOpenAccounts(String username) {
        loginPage.logIntoApplication(username, PASSWORD);

        accountsPage.clickAccountButton(AccountButton.SIDEBAR_ACCOUNTS);

        wait.until(ExpectedConditions.urlContains("/bank/accounts"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='account-row']")
        ));
    }

    private void switchUser(String username) {
        dashboardPage.clickLogoutButton();
        loginAndOpenAccounts(username);
    }

    // Verify Accounts Page
    @Test
    public void testAccountsPageDisplayed() {
        loginAndOpenAccounts(DEFAULT_USER);

        Assert.assertTrue(
                accountsPage.isAccountsPageDisplayed(),
                "Accounts page is not displayed."
        );
    }

    // TC-ACC-001 - Verify Accounts List Load
    @Test
    public void testAccountsListed() {
        loginAndOpenAccounts(DEFAULT_USER);

        List<String> allAccounts = accountsPage.getAllAccountRows();

        System.out.println("Accounts on page:");
        allAccounts.forEach(System.out::println);

        Assert.assertFalse(
                allAccounts.isEmpty(),
                "No accounts were loaded."
        );

        Assert.assertTrue(
                accountsPage.isAccountElementDisplayed(AccountType.CHECKING),
                "Checking account not found. Rows: " + allAccounts
        );

        Assert.assertTrue(
                accountsPage.isAccountElementDisplayed(AccountType.SAVINGS),
                "Savings account not found. Rows: " + allAccounts
        );
    }

    // TC-ACC-002 - Verify Account Details Display
    @Test
    public void testAccountDetailsDisplay() {
        loginAndOpenAccounts(DEFAULT_USER);

        Assert.assertTrue(
                accountsPage.isAccountNameListed(CHECKING_ACCOUNT),
                "Account name is not visible in the list."
        );

        String maskedNumber =
                accountsPage.getMaskedAccountNumber(CHECKING_ACCOUNT);

        System.out.println("Account: " + CHECKING_ACCOUNT);
        System.out.println("Masked Number: " + maskedNumber);

        Assert.assertTrue(
                maskedNumber.matches("\\*{4}\\d{4}"),
                "Account number is not properly masked: " + maskedNumber
        );

        accountsPage.viewAccount(CHECKING_ACCOUNT);

        Assert.assertEquals(
                accountsPage.getAccountElementText(AccountElement.DETAIL_NAME),
                CHECKING_ACCOUNT,
                "Account name does not match the details page."
        );
    }

    // TC-ACC-003 - Verify Overdraft Banner
    @Test
    public void testOverdraftBanner() {
        loginAndOpenAccounts("overdraft_user");

        Assert.assertTrue(
                accountsPage.isAccountElementDisplayed("Overdrawn"),
                "Overdraft warning indicator was not displayed."
        );
    }

    // TC-ACC-004 - Verify Frozen Account Alert
    @Test
    public void testFrozenAccountAlert() {
        loginAndOpenAccounts("frozen_user");

        Assert.assertTrue(
                accountsPage.isFreezeBannerDisplayed(),
                "Freeze banner is not displayed."
        );
    }

    // TC-ACC-006 - Verify Add Account Functionality
    @Test
    public void testAddNewAccount() {
        loginAndOpenAccounts(DEFAULT_USER);

        String accountName = "Automation Savings " + System.nanoTime();
        BigDecimal expectedBalance = new BigDecimal("2210.50");

        accountsPage.clickAccountButton(AccountButton.ADD_ACCOUNT);

        accountsPage.createAccount(
                accountName,
                AccountType.SAVINGS,
                expectedBalance.toPlainString()
        );

        wait.until(driver ->
                accountsPage.isAccountNameListed(accountName)
        );

        Assert.assertTrue(
                accountsPage.isAccountNameListed(accountName),
                "New account was not found: " + accountName
        );

        BigDecimal actualBalance =
                accountsPage.getAccountBalance(accountName);

        Assert.assertEquals(
                actualBalance.compareTo(expectedBalance),
                0,
                "New account balance does not match."
        );

        // Clean up the account created by this test.
        accountsPage.confirmDeleteAccount(accountName);

        wait.until(driver ->
                !accountsPage.isAccountNameListed(accountName)
        );
    }

    // Verify Delete Account Confirmation Dialog
    @Test
    public void testDeleteAccountButton() {
        loginAndOpenAccounts(DEFAULT_USER);

        accountsPage.clickDeleteAccount(CHECKING_ACCOUNT);

        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(
                                "[data-testid='confirm-delete-account-btn']"
                        )
                )).isDisplayed(),
                "Delete confirmation dialog was not displayed."
        );

        // Close dialog without deleting the default account.
        accountsPage.cancelDeleteAccountDialog();
    }

    // Verify Cancel Delete Account
    @Test
    public void testCancelDeleteAccount() {
        loginAndOpenAccounts(DEFAULT_USER);

        accountsPage.cancelDeleteAccount(CHECKING_ACCOUNT);

        Assert.assertTrue(
                accountsPage.isAccountNameListed(CHECKING_ACCOUNT),
                "Account should still exist after cancelling deletion."
        );
    }

    // Verify Confirm Delete Account
    @Test
    public void testConfirmDeleteAccount() {
        loginAndOpenAccounts(DEFAULT_USER);

        String accountName = "Delete Test " + System.nanoTime();

        accountsPage.clickAccountButton(AccountButton.ADD_ACCOUNT);

        accountsPage.createAccount(
                accountName,
                AccountType.SAVINGS,
                "100.00"
        );

        wait.until(driver ->
                accountsPage.isAccountNameListed(accountName)
        );

        Assert.assertTrue(
                accountsPage.isAccountNameListed(accountName),
                "Test account was not created."
        );

        accountsPage.confirmDeleteAccount(accountName);

        wait.until(driver ->
                !accountsPage.isAccountNameListed(accountName)
        );

        Assert.assertFalse(
                accountsPage.isAccountNameListed(accountName),
                "Account should be removed after confirming deletion."
        );
    }
}
