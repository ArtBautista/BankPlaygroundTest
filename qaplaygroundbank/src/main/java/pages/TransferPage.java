package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import base.BasePage;

public class TransferPage extends BasePage {

    private By transferFromButton = By.cssSelector("[data-testid='transfer-from-select']");
    private By transfertoButton = By.cssSelector("[data-testid='transfer-to-select']");
    private By transferAmountField = By.id("transfer-amount");
    private By transferReviewButton = By.cssSelector("[data-testid='review-transfer-btn']");
    private By errorMessage = By.xpath("//*[@data-testid='transfer-error-message']");
    private By billPaymentDateInput = By.id("transfer-scheduled-date");
    private By transferDateButton = By.cssSelector("[data-testid='date-type-scheduled']");
    private By transferSummary = By.cssSelector("[data-testid='transfer-confirm-summary']");

    public enum TransferButtons {
        SIDEBAR_TRANSFER_BUTTON("sidebar-link-transfer"),
        CANCEL_TRANSFER_BUTTON("cancel-transfer-btn"),
        REVIEW_TRANSFER_BUTTON("review-transfer-btn");
        

        private final String testId;

        TransferButtons(String testId) {
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
            return By.cssSelector("[data-testid='transfer-from-option'][data-account-id='" + accountId + "']");
        }

        public String getDisplayName() {
            return displayName;
        }
        
    }
    public enum ToAccount {
        HIGH_YIELD_SAVINGS("acc-savings-1", "High-Yield Savings"),
        EVERYDAY_CHECKING("acc-checking-1", "Everyday Checking");   

        private final String accountId;
        private final String displayName;

        ToAccount(String accountId, String displayName) {
            this.accountId = accountId;
            this.displayName = displayName;
        }

        By locator() {
            return By.cssSelector("[data-testid='transfer-to-option'][data-account-id='" + accountId + "']");
        }
        public String getDisplayName() {
            return displayName;
        }
    }
    public void clickTransferButtons(TransferButtons action){
        click(action.locator());
    }

    public List<String> getTransferSummary() {
        return getElements(transferSummary);
    }
    public boolean isAccountListed(FromAccount fromAccount, ToAccount toAccount, String amount, String date) {
        return getTransferSummary().stream()
            .anyMatch(row -> row.contains(fromAccount.getDisplayName())
                    && row.contains(toAccount.getDisplayName())
                    && row.contains(amount)
                    && row.contains(date));
    }
    public String getErrorMessage() {
        WebElement error = wait.until(
            ExpectedConditions.visibilityOfElementLocated(errorMessage)
        );
        return error.getText();
    }

    //Transfering Account
    public void selectFromAccount(FromAccount accountId){
        click(transferFromButton);
        click(accountId.locator());
    }
    public void selectToAccount(ToAccount accountId){
        click(transfertoButton);
        click(accountId.locator());
    }
    public void selectAccounts(FromAccount fromAccount,ToAccount toAccount){
        selectFromAccount(fromAccount);
        selectToAccount(toAccount);
    }
    public void enterTransferDetails(FromAccount fromAccount,ToAccount toAccount,String amount){
        selectAccounts(fromAccount,toAccount);
        enterTransferAmount(amount);
    }
    public void enterTransferAmount(String amount){
        set(transferAmountField, amount);
    }
    
    public void clickReviewTransfer(){
        click(transferReviewButton);
    }

    //Transfer Date
    public void enterTransferDate(String date){
        click(transferDateButton);
        set(billPaymentDateInput, date);
    }
    
}
