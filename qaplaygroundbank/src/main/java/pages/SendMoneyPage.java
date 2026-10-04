package pages;

import org.openqa.selenium.By;

import base.BasePage;


public class SendMoneyPage extends BasePage {

    private By sendMoneyFromButton = By.id("send-from-trigger");
    private By sendMoneyPayeeButton = By.id("payee-select-trigger");
    private By sendAmountField = By.id("send-amount");
    private By sendNoteField = By.id("send-note");


    public enum SendMoneyButtons {
        SIDEBAR_SENDMONEY_BUTTON("sidebar-link-send-money"),
        CANCEL_SENDMONEY_BUTTON("cancel-send-btn"),
        ADD_SENDMONEY_BUTTON("add-payee-btn"),
        REVIEW_SENDMONEY_BUTTON("review-send-btn");
        

        private final String testId;

        SendMoneyButtons(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }

    public void clickSendMoneyButtons(SendMoneyButtons action) {
        click(action.locator());
    }
    public enum FromAccount {
        HIGH_YIELD_SAVINGS("acc-savings-1", "High-Yield Savings"),
        EVERYDAY_CHECKING("acc-checking-1", "Everyday Checking");   // use your real ids

        private final String accountId;
        private final String displayName;

        FromAccount(String accountId, String displayName) {
            this.accountId = accountId;
            this.displayName = displayName;
        }
        
        By locator() {
            return By.cssSelector("[data-testid='send-from-option'][data-account-id='" + accountId + "']");
        }

        public String getDisplayName() {
            return displayName;
        }
        
    }
    public enum PayeeAccount {
        CHASE_BANK("payee-001", "High-Yield Savings"),
        BANK_OF_AMERICA("payee-002", "Everyday Checking");   

        private final String payeeId;
        private final String displayName;

        PayeeAccount(String payeeId, String displayName) {
            this.payeeId = payeeId;
            this.displayName = displayName;
        }

        By locator() {
            return By.cssSelector("[data-testid='payee-select-option'][data-payee-id='" + payeeId + "']");
        }
        public String getDisplayName() {
            return displayName;
        }
    }
    // Send Money
    public void selectFromAccount(FromAccount accountId){
        click(sendMoneyFromButton);
        click(accountId.locator());
    }
    public void selectPayeeAccount(PayeeAccount accountId){
        click(sendMoneyPayeeButton);
        click(accountId.locator());
    }
    public void selectSendMoneyAccounts(FromAccount fromAccount, PayeeAccount payeeAccount){
        selectFromAccount(fromAccount);
        selectPayeeAccount(payeeAccount);
    }
    //enterAmount
    public void enterSendMoneyAmount(String amount){
        set(sendAmountField, amount);
    }
    //Enter all details to send
    public void enterSendMoneyDetails(FromAccount fromAccount, PayeeAccount payeeAccount, String amount){
        selectSendMoneyAccounts(fromAccount,payeeAccount);
        enterSendMoneyAmount(amount);
    }

}
