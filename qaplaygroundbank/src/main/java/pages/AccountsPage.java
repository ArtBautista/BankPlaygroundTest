package pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BasePage;

public class AccountsPage extends BasePage{

    private By accountNameField = By.id("account-form-name");
    private By accountBalanceField = By.cssSelector("input[name='account_balance_field']");
    private By accountTypeField = By.cssSelector("[data-testid='account-form-type-select']");
    private By accountAcceptField = By.cssSelector("[data-testid='account-form-accept-terms-checkbox']");
    private By accountAddAccountButton = By.cssSelector("[data-testid='save-account-form-btn']");
    private By accountTable = By.xpath("[data-testid='accounts-row']");

    public enum AccountButtons {
        SIDEBAR_ACCOUNTS_BUTTON("sidebar-link-accounts"),
        ADD_ACTIONS_BUTTON("add-account-btn"),
        VIEW_ACTIONS_BUTTON("view-account-btn"),
        EDIT_ACTIONS_BUTTON("edit-account-btn");
        //REMOVE_ACTIONS_BUTTON("quick-action-bill-pay");
        

        private final String testId;

        AccountButtons(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }

    public enum AccountInformation {
        ACCOUNT_TITLE("accounts-page-title"),
        ACCOUNT_NAME("account-row-name"),
        ACCOUNT_TYPE("account-row-type-badge"),
        ACCOUNT_BALANCE("account-row-balance");
        //ACCOUNT_STATUS("accounts-status");

        private final String testId;

        AccountInformation(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }

    public enum AccountDetails {
        ACCOUNT_DETAILS_NAME("account-detail-name"),
        ACCOUNT_DETAILS_TYPE("account-detail-type-badge"),
        ACCOUNT_DETAILS_BALANCE("account-detail-balance");
        //ACCOUNT_STATUS("accounts-status");

        private final String testId;

        AccountDetails(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }

    public enum AccountType {
        CHECKING_TYPE("checking"),
        SAVINGS_TYPE("savings"),
        CREDIT_TYPE("credit");
        //REMOVE_ACTIONS_BUTTON("quick-action-bill-pay");
        

        private final String testId;

        AccountType(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='account-form-type-option'][data-account-type='" + testId + "']");
        }
    }

    public void clickAccountButtons(AccountButtons action) {
        click(action.locator());
    }
    
    public boolean isAccountsHeaderDisplayed(){
        return isDisplayed(AccountInformation.ACCOUNT_NAME.locator());
    }

    public List<String> getAllAccountRows() {
        List<WebElement> rows = driver.findElements(accountTable);
        return rows.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
    
    public String checkAccountDetails(AccountDetails details){
       return find(details.locator()).getText();
    }

    public String checkAccountInformation(AccountInformation info){
       return find(info.locator()).getText();
    }

    private void selectAccountType(AccountType type) {
        click(accountTypeField);
        click(type.locator());
    }
    
    public void enterAccountDetails(String name, AccountType type,String balance){
        set(accountNameField, name);
        selectAccountType(type);
        set(accountBalanceField, balance);   
    }
    public void createAccount(String name, AccountType type, String balance) {
        enterAccountDetails(name, type, balance);
        click(accountAcceptField);
        click(accountAddAccountButton);
    }

}
