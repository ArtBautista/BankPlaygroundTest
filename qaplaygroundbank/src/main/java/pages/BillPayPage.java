package pages;

import java.math.BigDecimal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BasePage;

public class BillPayPage extends BasePage {

    public enum BillPayButtons {
        REVIEW_PAYMENT_BUTTON("review-bill-btn"),
        CANCEL_PAYMENT_BUTTON("cancel-bill-btn"),
        ADD_BILLER_BUTTON("add-biller-btn"),
        SAVE_BILLER_BUTTON("save-add-biller-btn"),
        CANCEL_BILLER_BUTTON("cancel-add-biller-btn"),
        CONFIRM_BILLER_BUTTON("confirm-bill-btn"),
        PAYMENT_ERROR("bill-pay-error"),
        PAYMENT_SUCCESS("bill-pay-success-heading");
        

        private final String testId;

        BillPayButtons(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.cssSelector("[data-testid='" + testId + "']");
        }
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
            return By.cssSelector("[data-testid='bill-pay-from-option'][data-account-id='" + accountId + "']");
        }

        public String getDisplayName() {
            return displayName;
        }
        
    }

    public enum BillPayFields{
        BILLER_SEARCH_RESULTS("biller-search-results"),
        FROM_SENDMONEY_BUTTON("bill-pay-from-trigger"),
        BILLER_SENDMONEY_BUTTON("biller-search-input"),
        AMOUNT_SENDMONEY_BUTTON("bill-amount"),
        DATE_SENDMONEY_BUTTON("bill-payment-date"),
        NOTE_SENDMONEY_BUTTON("bill-memo-input");

        private final String testId;

        private BillPayFields(String testId) {
            this.testId = testId;
        }

        By locator() {
            return By.id(testId);
        }
    }
    public enum AddNewBillerFields{
        BILLER_NAME_FIELD(By.id("add-biller-name")),
        ACCOUNT_NUMBER_FIELD(By.name("biller_ref_field"));

        private final By locator;

        AddNewBillerFields(By locator) {
            this.locator = locator;
        }

        By locator() {
            return locator;
        }
    }
    public enum BillerAccount {
        CITY_ELECTRIC("City Electric Co."),
        METRO_WATER_UTILITY("Metro Water Utility");

        private final String displayName;

        BillerAccount(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    public void clickBillPayButtons(BillPayButtons action) {
        click(action.locator());
    }
    public void clickBillPayFields(BillPayFields action) {
        click(action.locator());
    }
    public void clickAddNewBillerFields(AddNewBillerFields action) {
        click(action.locator());
    }
    public void selectFromAccount(FromAccount accountId){
        clickBillPayFields(BillPayFields.FROM_SENDMONEY_BUTTON);
        click(accountId.locator());
    }
    public boolean isButtonDisplayed(BillPayButtons button) {
        return isDisplayed(button.locator());
    }
    public void selectBillerAccount(BillerAccount biller){
        clickBillPayFields(BillPayFields.BILLER_SENDMONEY_BUTTON);
        set(BillPayFields.BILLER_SENDMONEY_BUTTON.locator(), biller.getDisplayName());
        clickBillPayFields(BillPayFields.BILLER_SEARCH_RESULTS);
    }
    public String getSelectedBiller() {
        return find(
            BillPayFields.BILLER_SENDMONEY_BUTTON.locator()
        ).getDomProperty("placeholder").trim();
    }
    public boolean isBillPayFieldDisplayed(BillPayFields field) {
        return isDisplayed(field.locator());
    }
    public void enterBillerAmount(String amount){
        set(BillPayFields.AMOUNT_SENDMONEY_BUTTON.locator(), amount);
    }
    public void enterBillPayDate(String date){
        set(BillPayFields.DATE_SENDMONEY_BUTTON.locator(), date);
    }
    public void completeReview(FromAccount accountId, BillerAccount biller, String amount){
       selectFromAccount(accountId);
       selectBillerAccount(biller);
       enterBillerAmount(amount);
    }
    public void scheduleCompleteReview(FromAccount accountId, BillerAccount biller, String amount,String date){
       selectFromAccount(accountId);
       selectBillerAccount(biller);
       
       enterBillerAmount(amount);
       enterBillPayDate(date);
    }
    //ADD NEW BILLER
    public void setAddNewBillerFields(AddNewBillerFields field, String text){
        clickAddNewBillerFields(field);
        set(field.locator(), text);
    }
    public void completeAddNewBiller(String billerName, String accountNumber){
        clickBillPayButtons(BillPayButtons.ADD_BILLER_BUTTON);
        setAddNewBillerFields(AddNewBillerFields.BILLER_NAME_FIELD,billerName);
        setAddNewBillerFields(AddNewBillerFields.ACCOUNT_NUMBER_FIELD,accountNumber);
        clickBillPayButtons(BillPayButtons.SAVE_BILLER_BUTTON);
    }
    public BigDecimal getAccountBalance(FromAccount account) {

        // Open From Account dropdown
                clickBillPayFields(BillPayFields.FROM_SENDMONEY_BUTTON);


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
    //
    public BigDecimal selectFromAccountAndGetBalance(
            FromAccount account) {

        clickBillPayFields(BillPayFields.FROM_SENDMONEY_BUTTON);

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
}
