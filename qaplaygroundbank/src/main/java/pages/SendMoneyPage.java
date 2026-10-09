package pages;

import java.math.BigDecimal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BasePage;


public class SendMoneyPage extends BasePage {


    private By sendMoneyResult = By.cssSelector("[data-testid='send-success-heading']");
    private By sendMoneyFrozenBanner = By.cssSelector("[data-testid='send-money-frozen-banner']");
    private By errorMessage = By.cssSelector("[data-testid='add-payee-error-message']");
    private By insufficientFundsError = By.cssSelector("[data-testid='send-money-error']");
    public enum SendMoneyFields{
        FROM_SENDMONEY_BUTTON("send-from-trigger"),
        PAYEE_SENDMONEY_BUTTON("payee-select-trigger"),
        AMOUNT_SENDMONEY_BUTTON("send-amount"),
        NOTE_SENDMONEY_BUTTON("send-note");

        private final String testId;

        private SendMoneyFields(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.id(testId);
        }
    }
    public enum AddNewPayeeFields {
        PAYEE_NAME_FIELD("add-payee-name-input"),
        BANK_NAME_FIELD("add-payee-bank-input"),
        ROUTING_NUMBER_FIELD("add-payee-routing-input"),
        ACCOUNT_NUMBER_FIELD("add-payee-account-input");
        
        private final String testId;

        AddNewPayeeFields(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
    }
    public enum SendMoneyButtons {
        SIDEBAR_SENDMONEY_BUTTON("sidebar-link-send-money"),
        CANCEL_SENDMONEY_BUTTON("cancel-send-btn"),
        ADD_SENDMONEY_BUTTON("add-payee-btn"),
        SAVE_SENDMONEY_BUTTON("save-add-payee-btn"),
        CANCEL_ADD_SENDMONEY_BUTTON("cancel-add-payee-btn"),
        CONFIRM_SENDMONEY_BUTTON("confirm-send-btn"),
        CANCEL_CONFIRM_SENDMONEY_BUTTON("cancel-confirm-send-btn"),
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
    public void clickSendMoneyFields(SendMoneyFields action) {
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
    public BigDecimal getAccountBalance(FromAccount account) {

        // Open From Account dropdown
        clickSendMoneyFields(
            SendMoneyFields.FROM_SENDMONEY_BUTTON
        );

        // Retrieve account text
        WebElement accountElement = find(account.locator());

        String accountText = accountElement.getText().trim();

        // Example: Everyday Checking — $4,175.00
        String balance = accountText
            .split("—")[1]
            .replace("$", "")
            .replace(",", "")
            .trim();

        return new BigDecimal(balance);
    }

    // Retrieve balance and select From Account
    public BigDecimal selectFromAccountAndGetBalance(
            FromAccount account) {

        clickSendMoneyFields(
            SendMoneyFields.FROM_SENDMONEY_BUTTON
        );

        WebElement accountElement = find(account.locator());

        String accountText = accountElement.getText().trim();

        String balance = accountText
            .split("—")[1]
            .replace("$", "")
            .replace(",", "")
            .trim();

        BigDecimal accountBalance = new BigDecimal(balance);

        click(account.locator());

        return accountBalance;
    }
    // Generate amount greater than available balance
    public BigDecimal getInsufficientAmount(FromAccount account) {
        return getAccountBalance(account)
            .add(new BigDecimal("100.00"));
    }
    public boolean isInsufficientFundsDisplayed() {
        return isDisplayed(insufficientFundsError);
    }
    public boolean isFrozenAccountDisplayed() {
        return isDisplayed(sendMoneyFrozenBanner);
    }
    public boolean isSendMoneyButtonEnabled(SendMoneyButtons button) {
        return find(button.locator()).isEnabled();
    }
    // Send Money
    public void selectFromAccount(FromAccount accountId){
        clickSendMoneyFields(SendMoneyFields.FROM_SENDMONEY_BUTTON);
        click(accountId.locator());
    }
    public void selectPayeeAccount(PayeeAccount accountId){
        clickSendMoneyFields(SendMoneyFields.PAYEE_SENDMONEY_BUTTON);
        click(accountId.locator());
    }
    public void selectSendMoneyAccounts(FromAccount fromAccount, PayeeAccount payeeAccount){
        selectFromAccount(fromAccount);
        selectPayeeAccount(payeeAccount);
    }
    //enterAmount
    public void enterSendMoneyAmount(String amount){
        set(SendMoneyFields.AMOUNT_SENDMONEY_BUTTON.locator(), amount);
    }
    //Enter all details to send
    public void enterSendMoneyDetails(FromAccount fromAccount, PayeeAccount payeeAccount, String amount){
        selectSendMoneyAccounts(fromAccount,payeeAccount);
        enterSendMoneyAmount(amount);
    }
    //Enter all details to send
    public void compeleteSendMoney(FromAccount fromAccount, PayeeAccount payeeAccount, String amount){
        enterSendMoneyDetails(fromAccount,payeeAccount,amount);
        clickSendMoneyButtons(SendMoneyButtons.REVIEW_SENDMONEY_BUTTON);
        clickSendMoneyButtons(SendMoneyButtons.CONFIRM_SENDMONEY_BUTTON);
    }
    //
    public boolean isSendMoneyFieldDisplayed(SendMoneyFields field) {
        return isDisplayed(field.locator());
    }
    public boolean isSendMoneyButtonsDisplayed(SendMoneyButtons button) {
        return isDisplayed(button.locator());
    }
    public String getSendMoneySuccessMessage() {
        return find(sendMoneyResult).getText().trim();
    }
    //Add a new payee 
    //Enter Add Amount
    public void enterAddNewPayeeInput(AddNewPayeeFields addPayeeInput,String info){
        set(addPayeeInput.locator(), info);
    }
    //Enter Add Amount
    public void enterAddNewDetails(String name, String bank, String routing, String accountNum){
        clickSendMoneyButtons(SendMoneyButtons.ADD_SENDMONEY_BUTTON);
        enterAddNewPayeeInput(AddNewPayeeFields.PAYEE_NAME_FIELD,name);
        enterAddNewPayeeInput(AddNewPayeeFields.BANK_NAME_FIELD,bank);
        enterAddNewPayeeInput(AddNewPayeeFields.ROUTING_NUMBER_FIELD,routing);
        enterAddNewPayeeInput(AddNewPayeeFields.ACCOUNT_NUMBER_FIELD,accountNum);
    }
    public String getSAddNewPayeeMessage() {
        return find(errorMessage).getText().trim();
    }
}
