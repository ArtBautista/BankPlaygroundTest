package pages;

import java.math.BigDecimal;
import java.util.List;

import org.openqa.selenium.By;

import base.BasePage;

public class AccountsPage extends BasePage {

    // Locators
    private final By accountNameField = By.id("account-form-name");
    private final By accountBalanceField = By.cssSelector("input[name='account_balance_field']");
    private final By accountTypeField = By.cssSelector("[data-testid='account-form-type-select']");
    private final By accountAcceptTermsCheckbox = By.cssSelector("[data-testid='account-form-accept-terms-checkbox']");
    private final By saveAccountButton =By.cssSelector("[data-testid='save-account-form-btn']");
    private final By accountRows = By.cssSelector("[data-testid='account-row']");
    private final By confirmDeleteButton = By.cssSelector("[data-testid='confirm-delete-account-btn']");
    private final By cancelDeleteButton = By.cssSelector("[data-testid='cancel-delete-account-btn']");
    private final By accountFreezeBanner = By.cssSelector("[data-testid='frozen-account-banner']");


    // Account Buttons
    public enum AccountButton {
        SIDEBAR_ACCOUNTS("sidebar-link-accounts"),
        ADD_ACCOUNT("add-account-btn"),
        VIEW_ACCOUNT("view-account-btn"),
        EDIT_ACCOUNT("edit-account-btn");

        private final String testId;

        AccountButton(String testId) {
            this.testId = testId;
        }

        public By locator() {
            return By.cssSelector(
                    "[data-testid='" + testId + "']"
            );
        }
    }

    // Account Elements
    public enum AccountElement {

        // Accounts List
        PAGE_TITLE("accounts-page-title"),
        LIST_NAME("account-row-name"),
        LIST_TYPE("account-row-type-badge"),
        LIST_BALANCE("account-row-balance"),
        LIST_STATUS("account-row-status"),

        // Account Details
        DETAIL_NAME("account-detail-name"),
        DETAIL_TYPE("account-detail-type-badge"),
        DETAIL_BALANCE("account-detail-balance"),

        // Existing Accounts
        HIGH_YIELD_SAVINGS("High-Yield Savings"),    
        EVERYDAY_CHECKING( "Everyday Checking");

        private final String testId;

        AccountElement(String testId) {
            this.testId = testId;
        }

        public By locator() {
            return By.cssSelector(
                    "[data-testid='" + testId + "']"
            );
        }
        
    }

    // Account Types
    public enum AccountType {
        CHECKING("checking"),
        SAVINGS("savings"),
        CREDIT("credit");

        private final String accountType;

        AccountType(String accountType) {
            this.accountType = accountType;
        }

        public By locator() {
            return By.cssSelector(
                    "[data-testid='account-form-type-option']"
                    + "[data-account-type='" + accountType + "']"
            );
        }
        
    }

    // Account History
    public static class AccountHistory {

        private final String listValue;
        private final String detailValue;

        public AccountHistory(String listValue, String detailValue) {
            this.listValue = listValue;
            this.detailValue = detailValue;
        }

        public String getListValue() {
            return listValue;
        }

        public String getDetailValue() {
            return detailValue;
        }
    }


    // Account Navigation
    public void clickAccountButton(AccountButton button) {
        click(button.locator());
    }


    // Account Information
    public List<String> getAllAccountRows() {
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Row count: " + driver.findElements(accountRows).size());
        return getElements(accountRows);
    }
    public String getAccountElementText(AccountElement element) {
        return find(element.locator()).getText();
    }
    public List<String> getAccountTypes() {
        return driver.findElements(accountRows)
                .stream()
                .map(row -> row.getAttribute("data-account-type"))
                .toList();
    }
    public List<String> getAccountStatus() {
        return driver.findElements(accountRows)
                .stream()
                .map(row -> row.getAttribute("account-row-overdrawn"))
                .toList();
    }
    public String getAccountsTotalBalance(AccountElement element) {
        return find(element.locator()).getText();
    }
    public boolean isAccountsPageDisplayed() {
        return isDisplayed(AccountElement.PAGE_TITLE.locator());
    }
    public boolean isFreezeBannerDisplayed() {
        System.out.println(find(accountFreezeBanner).getText());
        return isDisplayed(accountFreezeBanner);
    }
    public boolean isAccountElementDisplayed(String accountElement) {
        return getAllAccountRows()
                .stream()
                .anyMatch(row -> row.contains("Overdrawn"));
    }

    public void viewAccount(String accountName) {
        By viewButton = By.xpath(
                "//tr[@data-testid='account-row']" +
                "[.//*[@data-testid='account-row-name' and text()='"
                + accountName +
                "']]//*[@data-testid='view-account-btn']"
        );

        click(viewButton);
    }
    public boolean isAccountDetailNameDisplayed() {
        return isDisplayed(AccountElement.DETAIL_NAME.locator());
    }
    public String getAccountDetailName() {
        return getAccountElementText(AccountElement.DETAIL_NAME);
    }
    // Account History
    public AccountHistory getAccountHistory(AccountElement listElement, AccountElement detailElement) {

        String listValue = getAccountElementText(listElement);
        System.out.println("ACCOUNT LIST: " + listValue);

        clickAccountButton(AccountButton.VIEW_ACCOUNT);

        String detailValue = getAccountElementText(detailElement);
        System.out.println("ACCOUNT DETAILS: " + detailValue);

        return new AccountHistory(listValue, detailValue);
    }


    // Create Account
    private void selectAccountType(AccountType type) {
        click(accountTypeField);
        click(type.locator());
    }
    public void enterAccountDetails( String name, AccountType type, String balance) {

        set(accountNameField, name);
        selectAccountType(type);
        set(accountBalanceField, balance);
    }
    public void createAccount(String name,AccountType type,String balance) {

        enterAccountDetails(name, type, balance);
        click(accountAcceptTermsCheckbox);
        click(saveAccountButton);
    }


    // Delete Account
    public void clickDeleteAccount(String accountName) {
        By deleteButton = By.cssSelector( "button[aria-label='Delete " + accountName + "']" );
        click(deleteButton);
    }
    public void confirmDeleteAccount(String accountName) {
        clickDeleteAccount(accountName);
        click(confirmDeleteButton);
    }
    public void cancelDeleteAccount(String accountName) {
        clickDeleteAccount(accountName);
        click(cancelDeleteButton);
    }

    // Check Balance Account
    public BigDecimal getTotalAccountBalance() {
        BigDecimal totalBalance = driver
                .findElements(AccountElement.LIST_BALANCE.locator())
                .stream()
                .map(element -> element.getAttribute("data-balance"))
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2);

        System.out.println("Total Account Balance: $" + totalBalance);

        return totalBalance;
    }
    public BigDecimal getAccountBalance(String accountName) {
        By balanceLocator = By.xpath(
            "//tr[@data-testid='account-row']" +
            "[.//*[@data-testid='account-row-name' and text()='"
            + accountName +
            "']]//*[@data-testid='account-row-balance']"
        );

        String balance = find(balanceLocator).getAttribute("data-balance");

        return new BigDecimal(balance).setScale(2);
    }
    //GET MASKED NUMBER
    public String getMaskedAccountNumber(String accountName) {
        By accountNumber = By.xpath(
                "//tr[@data-testid='account-row']" +
                "[.//*[@data-testid='account-row-name' and text()='"
                + accountName +
                "']]//p[contains(text(), '****')]"
        );

        return find(accountNumber).getText();
    }
}