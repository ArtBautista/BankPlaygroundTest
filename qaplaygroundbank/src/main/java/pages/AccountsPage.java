package pages;

import org.openqa.selenium.By;

import base.BasePage;

public class AccountsPage extends BasePage{
    private static final By ADD_ACCOUNT_BUTTON = By.cssSelector("[data-testid='add-account-btn']");
    private static final By ACCOUNT_TITLE = By.cssSelector("[data-testid='accounts-page-title']");
    private static final By ACCOUNT_NAME = By.cssSelector("[data-testid='add-account-btn']");
    private static final By ACCOUNT_TYPE = By.cssSelector("[data-testid='add-account-btn']");
    private static final By ACCOUNT_BALANCE = By.cssSelector("[data-testid='add-account-btn']");
    private static final By ACCOUNT_STATUS = By.cssSelector("[data-testid='add-account-btn']");
    private static final By VIEW_ACTIONS_BUTTONS = By.cssSelector("[data-testid='add-account-btn']");
    private static final By EDIT_ACTIONS_BUTTONS = By.cssSelector("[data-testid='add-account-btn']");
    private static final By REMOVE_ACTIONS_BUTTONS = By.cssSelector("[data-testid='add-account-btn']");

    private static final By SIDEBAR_ACCOUNTS_BUTTON = By.cssSelector("[data-testid='sidebar-link-accounts']");

    public void clickSidebarAccountsButton(){
        click(SIDEBAR_ACCOUNTS_BUTTON);
    }

    public boolean isAccountsHeaderDisplayed(){
        return isDisplayed(ACCOUNT_TITLE);
    }

    

}
