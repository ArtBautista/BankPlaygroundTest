
package pages;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;

public class AccountsPage extends BasePage {

    // Locators
    private final By accountNameField =
            By.id("account-form-name");

    private final By accountBalanceField =
            By.cssSelector("input[name='account_balance_field']");

    private final By accountTypeField =
            By.cssSelector("[data-testid='account-form-type-select']");

    private final By accountAcceptTermsCheckbox =
            By.cssSelector("[data-testid='account-form-accept-terms-checkbox']");

    private final By saveAccountButton =
            By.cssSelector("[data-testid='save-account-form-btn']");

    private final By accountRows =
            By.cssSelector("[data-testid='account-row']");

    private final By confirmDeleteButton =
            By.cssSelector("[data-testid='confirm-delete-account-btn']");

    private final By cancelDeleteButton =
            By.cssSelector("[data-testid='cancel-delete-account-btn']");

    private final By accountFreezeBanner =
            By.cssSelector("[data-testid='frozen-account-banner']");

    private final By overdraftIndicator =
            By.xpath("//*[normalize-space(text())='Overdrawn']");

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

        // Account Details
        DETAIL_NAME("account-detail-name"),
        DETAIL_TYPE("account-detail-type-badge"),
        DETAIL_BALANCE("account-detail-balance");

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

        public String getValue() {
            return accountType;
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

    public boolean isAccountsPageDisplayed() {
        return isDisplayed(AccountElement.PAGE_TITLE.locator());
    }

    public boolean isAccountElementDisplayed(AccountType type) {
        By locator = By.cssSelector(
                "[data-testid='account-row'][data-account-type='"
                + type.getValue() + "']"
        );

        return driver.findElements(locator)
                .stream()
                .anyMatch(WebElement::isDisplayed);
    }

    // Text-based status/indicator check
    public boolean isAccountElementDisplayed(String text) {
        return driver.findElements(
                By.xpath("//*[normalize-space(text())='"
                        + text + "']")
        ).stream().anyMatch(WebElement::isDisplayed);
    }

    public boolean isAccountNameListed(String accountName) {
        return driver.findElements(AccountElement.LIST_NAME.locator())
                .stream()
                .anyMatch(element ->
                        element.isDisplayed()
                        && element.getText().equals(accountName)
                );
    }

    public boolean isFreezeBannerDisplayed() {
        return driver.findElements(accountFreezeBanner)
                .stream()
                .anyMatch(WebElement::isDisplayed);
    }

    public boolean isOverdraftIndicatorDisplayed() {
        return driver.findElements(overdraftIndicator)
                .stream()
                .anyMatch(WebElement::isDisplayed);
    }

    // View Account
    public void viewAccount(String accountName) {
        WebElement row = getAccountRow(accountName);

        row.findElement(AccountButton.VIEW_ACCOUNT.locator())
                .click();
    }

    public boolean isAccountDetailNameDisplayed() {
        return isDisplayed(AccountElement.DETAIL_NAME.locator());
    }

    public String getAccountDetailName() {
        return getAccountElementText(AccountElement.DETAIL_NAME);
    }

    // Account History
    public AccountHistory getAccountHistory(
            AccountElement listElement,
            AccountElement detailElement) {

        String listValue = getAccountElementText(listElement);

        clickAccountButton(AccountButton.VIEW_ACCOUNT);

        String detailValue = getAccountElementText(detailElement);

        return new AccountHistory(listValue, detailValue);
    }

    // Create Account
    private void selectAccountType(AccountType type) {
        click(accountTypeField);
        click(type.locator());
    }

    public void enterAccountDetails(
            String name,
            AccountType type,
            String balance) {

        set(accountNameField, name);
        selectAccountType(type);
        set(accountBalanceField, balance);
    }

    public void createAccount(
            String name,
            AccountType type,
            String balance) {

        enterAccountDetails(name, type, balance);

        click(accountAcceptTermsCheckbox);
        click(saveAccountButton);
    }

    // Delete Account
    public void clickDeleteAccount(String accountName) {
        WebElement row = getAccountRow(accountName);

        row.findElement(
                By.cssSelector("button[aria-label^='Delete ']")
        ).click();
    }

    public void confirmDeleteAccount(String accountName) {
        clickDeleteAccount(accountName);
        click(confirmDeleteButton);
    }

    public void cancelDeleteAccount(String accountName) {
        clickDeleteAccount(accountName);
        cancelDeleteAccountDialog();
    }

    public void cancelDeleteAccountDialog() {
        click(cancelDeleteButton);
    }

    // Account Balances
    public BigDecimal getTotalAccountBalance() {
        return driver.findElements(AccountElement.LIST_BALANCE.locator())
                .stream()
                .map(element -> element.getAttribute("data-balance"))
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2);
    }

    public BigDecimal getAccountBalance(String accountName) {
        WebElement row = getAccountRow(accountName);

        String balance = row.findElement(
                AccountElement.LIST_BALANCE.locator()
        ).getAttribute("data-balance");

        return new BigDecimal(balance).setScale(2);
    }

    // Masked Account Number
    public String getMaskedAccountNumber(String accountName) {
        WebElement row = getAccountRow(accountName);

        return row.findElement(
                By.cssSelector("td:first-child p.font-mono")
        ).getText().trim();
    }

    // Reusable Account Row Finder
    private WebElement getAccountRow(String accountName) {
        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(10)
        );

        return wait.until(driver ->
                driver.findElements(accountRows)
                        .stream()
                        .filter(row -> row.findElement(
                                AccountElement.LIST_NAME.locator()
                        ).getText().equals(accountName))
                        .findFirst()
                        .orElse(null)
        );
    }
}
