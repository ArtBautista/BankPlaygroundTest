package pages;

import org.openqa.selenium.By;

import base.BasePage;

public class AccountsPage extends BasePage{

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

    public void clickAccountButtons(AccountButtons action) {
        click(action.locator());
    }
    
    public boolean isAccountsHeaderDisplayed(){
        return isDisplayed(AccountInformation.ACCOUNT_NAME.locator());
    }

    public String checkAccountDetails(AccountDetails details){
       return find(details.locator()).getText();
    }

    public String checkAccountInformation(AccountInformation info){
       return find(info.locator()).getText();
    }

}
